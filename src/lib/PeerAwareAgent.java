package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lib.entities.SearchRequest;

/**
 * provides everything an agent needs in order to register new peers and forward requests
 */
public class PeerAwareAgent extends Agent {
	
	private static int maxForwardsPerSearchRequest = 3;
	private HashMap<Integer, SearchRequest> requestsForwarded = new HashMap<Integer, SearchRequest>(); 

	protected Set<SearchRequest> workingOn = new HashSet<SearchRequest>();
	protected List<AID> peers = new ArrayList<AID>();
	protected List<AID> availablePeers = new ArrayList<AID>();
	private TreeMap<AID, SearchRequest> searchRequestQueue = new TreeMap<AID, SearchRequest>();
	
	public PeerAwareAgent() {

	}
	
	protected void addRequestForForwarding(SearchRequest request) {
		searchRequestQueue.put(request.getInitiator(), request);
	}

	protected void log(String message) {
		System.out.println(message);
	}

	@Override
	protected void setup() {
		// look for agents which have registered as "peers" every 10 seconds
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
						if (!peers.contains(name) && !name.equals(PeerAwareAgent.this.getAID())) {	// dont add yourself to the list of available peers
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
		super.setup();
	}
	
	protected class RequestForwarder extends TickerBehaviour {

		public RequestForwarder(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 123L;

		@Override
		public void onTick() {
			// log("ParseRequestPerformer action()");
			try {
				log("Available Peers: " + availablePeers.size() + ", search queue size: "
						+ searchRequestQueue.size());
				while (searchRequestQueue.size() > 0) {
					SearchRequest request = searchRequestQueue.firstEntry().getValue();

					// check whether the searchrequest was already forwarded once we
					// dont want to forward the same request more than once (avoid cyclic forwarding behavior)
					// TODO: we need a timeout implementation here
					if(!requestsForwarded.containsKey(request.getId())){
						boolean wasForwarded = forwardRequest(request);
						if(wasForwarded){
							requestsForwarded.put(request.getId(), request);
						}
					}
					
					requestsForwarded.put(request.getId(), request);
				}
			} catch (NoSuchElementException e) {
				log("Queue is empty.");
			}

			if (searchRequestQueue.size() == 0) {
				log("Nothing to do. Waiting, waiting, waiting...Boooooring!");
			}
		}
		
		private boolean forwardRequest(SearchRequest request) {
			boolean wasForwarded = false;
			
			for(int i=0; i< PeerAwareAgent.maxForwardsPerSearchRequest; i++){
				
				if(availablePeers.size() > 0){
					AID selectedPeer = availablePeers.remove(0);
					log("forward fingerPrint to peer " + selectedPeer.getLocalName() + ", fingerPrint: " + request);
					ACLMessage message = new ACLMessage(ACLMessage.CFP);
					message.addReceiver(selectedPeer);
					message.setContent(request.serialize());
					message.setConversationId("search-fingerprint");
					message.setReplyWith("message_" + selectedPeer + "_" + System.currentTimeMillis());
					myAgent.send(message);
					searchRequestQueue.remove(request.getInitiator());
					wasForwarded = true;
				} else {
					log("no more agents available right now");
					break;
				}
			}
			
			return wasForwarded;

		}

	}
}
