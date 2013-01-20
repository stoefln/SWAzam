package server;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

import lib.dao.RequestDAO;
import lib.dao.UserDAO;
import lib.entities.Request;
import lib.entities.SearchRequest;
import lib.entities.SearchResponse;
import lib.entities.User;
import lib.utils.Utility;

public class ServerAgent extends Agent {

	private static final long serialVersionUID = 2L;

	private TreeMap<AID, SearchRequest> fingerPrintSearchQueue = new TreeMap<AID, SearchRequest>();
	private Set<SearchRequest> workingOn = new HashSet<SearchRequest>();
	private List<AID> peers = new ArrayList<AID>();
	private List<AID> availablePeers = new ArrayList<AID>();

	private RequestForwarder requestForwarder;
	private ResponseReceiver responseReceiver;
	private RequestReceiver requestReceiver;
	private UserDAO userDAO = new UserDAO();
	private User requestingUser;
	private RequestDAO requestDAO = new RequestDAO();
	private Request requestDB; 
	
	protected void log(String message) {
		System.out.println(message);
	}

	@Override
	protected void setup() {
		System.out.println("Hallo I'm the ServerAgent! My name is " + getAID().getName());

		//SearchRequestDAO dao = new SearchRequestDAO();
		//dao.setPersistenceUnit("swazam_server");
		//SearchRequest searchRequest = new SearchRequest();
		//searchRequest.setAcessToken("test");
		//dao.persist(searchRequest);

		Object[] args = getArguments();
		if (args != null && args.length != 0) {
			if (args.length > 0) {

			}

		} else {

			// doDelete();
			// return;
		}

		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("SWAzamServer");
		serviceDescription.setName("SWAzam Server");
		agentDescription.addServices(serviceDescription);
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// 1) look for agents which have registered as "peers" every 10 seconds
		addBehaviour(new TickerBehaviour(this, 10000) {

			private static final long serialVersionUID = 1L;

			protected void onTick() {
				// log("Update the list of Peer agents.");
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("SWAzamPeer");
				template.addServices(sd);
				try {
					// log("All agents: ");
					DFAgentDescription[] result = DFService.search(myAgent, template);

					for (int i = 0; i < result.length; ++i) {
						AID name = result[i].getName();
						if (!peers.contains(name)) {
							availablePeers.add(name);
							peers.add(name);
							log("new peer registered: " + result[i].getName());
						}
						// log("  "+result[i].getName());
					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}

			}
		});

		// 2) receive requests from clients
		requestReceiver = new RequestReceiver();
		addBehaviour(requestReceiver);

		// 3) loop through all available peers and forward a
		// searchRequest/fingerPrint to each of
		// them (as long as there are fingerPrints)
		requestForwarder = new RequestForwarder(this, 4000);
		addBehaviour(requestForwarder);

		// 4) get responses, do transaction stuff (coins) and forward the
		// response back to the client
		responseReceiver = new ResponseReceiver();
		addBehaviour(responseReceiver);
		super.setup();
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		log("ServerAgent " + getAID().getName() + " sais good bye");
	}

	private class RequestReceiver extends CyclicBehaviour {

		private static final long serialVersionUID = 24L;

		@Override
		public void action() {

			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = myAgent.receive(mt);

			if (message != null) {
				SearchRequest request;
				
				try {
					request = (SearchRequest) Utility.fromString(message.getContent());
					// server.newRequest(request);
					// TODO Catch eventual exception, should a server-object not
					// have been set?
					requestingUser = (User) userDAO.findByToken(request.getAccessToken()).get(0);
					requestDB = new Request(requestingUser, new Date());
					requestDAO.persist(requestDB);
					
					log("Request received from client with fingerPrint \"" + request.getFingerprint() + "\"");
					fingerPrintSearchQueue.put(request.getInitiator(), request);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			} else {
				block();
			}

		}

	}

	private class RequestForwarder extends TickerBehaviour {

		public RequestForwarder(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 123L;

		@Override
		public void onTick() {
			// log("ParseRequestPerformer action()");
			try {
				log("Available Peers: " + availablePeers.size() + ", search queue size: "
						+ fingerPrintSearchQueue.size());
				while (availablePeers.size() > 0 && fingerPrintSearchQueue.size() > 0) {
					SearchRequest request = fingerPrintSearchQueue.firstEntry().getValue();

					// check whether the searchrequest is still going on. we
					// dont want to forward the same request more than once
					// TODO: we need a timeout implementation here
					if (!workingOn.contains(request)) {
						workingOn.add(request);
						AID selectedPeer = availablePeers.remove(0);
						log("forward fingerPrint to peer " + selectedPeer.getLocalName() + ", fingerPrint: " + request);
						ACLMessage message = new ACLMessage(ACLMessage.CFP);
						message.addReceiver(selectedPeer);
						message.setContent(request.serialize());
						message.setConversationId("search-fingerprint");
						message.setReplyWith("message_" + selectedPeer + "_" + System.currentTimeMillis());
						myAgent.send(message);
						fingerPrintSearchQueue.remove(request.getInitiator());

						// server.requestResent(request);
					} else {
						log("Removing duplicate entry " + request);
					}
				}
			} catch (NoSuchElementException e) {
				log("Queue is empty.");
			}

			if (fingerPrintSearchQueue.size() == 0) {
				log("Nothing to do. Waiting, waiting, waiting...Boooooring!");
			}
		}

	}

	private class ResponseReceiver extends CyclicBehaviour {

		private static final long serialVersionUID = 23L;

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
					MessageTemplate.MatchPerformative(ACLMessage.REFUSE));
			ACLMessage responseMessage = myAgent.receive(mt);
			if (responseMessage != null) {
				if (responseMessage.getPerformative() == ACLMessage.CONFIRM) {
					AID aid = responseMessage.getSender();
					if (peers.contains(aid)) {
						availablePeers.add(aid); // Peer has done its job and is
													// now
													// queued again for the next
													// one
					}

					SearchResponse searchResponse = null;
					try {
						searchResponse = (SearchResponse) Utility.fromString(responseMessage.getContent());
					} catch (IOException e) {
						e.printStackTrace();
						return;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}

					workingOn.remove(searchResponse.getFingerPrint());

					// TODO: coin transfer
					// TODO: forward the response back to the client
					if (searchResponse.wasFound()) {

						ACLMessage message = new ACLMessage(ACLMessage.CONFIRM);
						message.addReceiver(searchResponse.getSearchRequest().getInitiator());
						message.setContent(searchResponse.serialize());
						message.setConversationId("found-fingerPrint");
						message.setReplyWith("message_" + myAgent.getName() + "_" + System.currentTimeMillis());
						myAgent.send(message);
						log("forwarding search response from peer to client: " + searchResponse.toString());

						// server.newResponse(searchResponse);

					} else {
						// TODO: notify client
						log("no response found (timed out)");
					}

				} else if (responseMessage.getPerformative() == ACLMessage.REFUSE) {
					log("Peer rejected because: " + responseMessage.getContent());
				}

			} else {
				block();
			}

		}

	}

}
