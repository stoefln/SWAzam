package net.microtrash;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class ServerAgent extends Agent {

	private static final long serialVersionUID = 2L;

	private Deque<String> fingerPrintSearchQueue = new ArrayDeque<String>();
	private Set<String> workingOn = new HashSet<String>();
	private List<AID> peers = new ArrayList<AID>();
	private List<AID> availablePeers = new ArrayList<AID>();

	private RequestSender requestSender;
	private ResponseReceiver responseReceiver;
	
	protected void log(String message) {
		System.out.println(message);
	}
	@Override
	protected void setup() {
		System.out.println("Hallo I'm the ServerAgent! My name is "+getAID().getName());
		
		Object[] args = getArguments(); 
		if (args != null && args.length != 0) {
			if(args.length > 0){
				
			}
			
		}else{
			
			//doDelete();
			//return;
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
				//log("Update the list of Peer agents.");
				DFAgentDescription template = new DFAgentDescription(); 
				ServiceDescription sd = new ServiceDescription(); 
				sd.setType("SWAzamPeer"); 
				template.addServices(sd);
				try {
					//log("All agents: ");
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					for (int i = 0; i < result.length; ++i) {
						AID name = result[i].getName();
						if(!peers.contains(name)) {
							availablePeers.add(name);
							peers.add(name);
							log("new peer registered: " + result[i].getName());
						}
						//log("  "+result[i].getName());
					}
				}catch(FIPAException fe) {
					fe.printStackTrace();
				}
				
			}			
		});
		
		// 2) loop through all available peers and push a fingerprint to each of them (as long as there are fingerprints) 
		requestSender = new RequestSender(this, 4000);
		addBehaviour(requestSender);
		
		// 3) get responses, do transaction stuff (coins) and forward the response back to the client
		responseReceiver = new ResponseReceiver();
		addBehaviour(responseReceiver);
		super.setup();
	}
	
	@Override
	protected void takeDown() { 
		log("ServerAgent "+getAID().getName()+" sais good bye");
	}
	

	
	private class RequestSender extends TickerBehaviour{
		
		public RequestSender(Agent a, long period) {
			super(a, period);
		}


		private static final long serialVersionUID = 123L;

		
		@Override
		public void onTick() {
			//log("ParseRequestPerformer action()");
			try{
				log("Available Peers: " + availablePeers.size() + ", search queue size: " + fingerPrintSearchQueue.size());
				while(availablePeers.size() > 0) {
					String fingerPrint = fingerPrintSearchQueue.removeFirst();
					// check whether the searchrequest is still going on.
					// TODO: we need a timeout implementation here
					if(!workingOn.contains(fingerPrint)) {	
						workingOn.add(fingerPrint);
						AID aid = availablePeers.remove(0);
						log("send fingerprint to agent"+aid.getLocalName()+": "+fingerPrint);
						ACLMessage message = new ACLMessage(ACLMessage.CFP);
						message.addReceiver(aid);
						message.setContent(fingerPrint);
						message.setConversationId("search-fingerprint");
						message.setReplyWith("message_"+aid+"_"+System.currentTimeMillis());
						myAgent.send(message);
					} else {
						log("Removing duplicate entry (" + fingerPrint);
					}
				}
			}catch(NoSuchElementException e){
				log("Queue is empty.");
			} 
			
			if(fingerPrintSearchQueue.size() == 0){
				log("Nothing to do. Waiting, waiting, waiting...Boooooring!");
			}
		}

		
	}

	private class ResponseReceiver extends CyclicBehaviour{
		
		private static final long serialVersionUID = 23L;

		@Override
		public void action() {
			ACLMessage reply = myAgent.receive();
			
			if (reply != null) {
				AID aid = reply.getSender();
				if(peers.contains(aid)) {
					availablePeers.add(aid);	// Peer has done its job and is now queued again for the next one
				}
				if(reply.getPerformative() == ACLMessage.CONFIRM) {
					String serialisedSearchResponse = reply.getContent();
					SearchResponse searchResponse = null;
					try {
						searchResponse = (SearchResponse) Utility.fromString(serialisedSearchResponse);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					
					workingOn.remove(searchResponse.getFingerPrint());
					
					//TODO: coin transfer
					//TODO: forward the response back to the client
					
					
					
					
					
					
					log("search response found! " + searchResponse.toString());
				} else {
					log("Peer rejected because: " + reply.getContent());
				}
			} else {
				block();
			}

		}

		
	}
}

