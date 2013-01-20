package peer;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import lib.PeerAwareAgent;
import lib.entities.SearchRequest;
import lib.entities.SearchResponse;
import lib.entities.Song;
import lib.manager.SongManager;
import lib.utils.Utility;
import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class PeerAgent extends PeerAwareAgent {

	private static final long serialVersionUID = 1L;
	
	
	
	protected void log(String message) {
		System.out.println(message);
	}

	@Override
	protected void setup() {
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("SWAzamPeer");
		serviceDescription.setName("SWAzam Peer");
		agentDescription.addServices(serviceDescription);
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new requestReceiver());
		
		System.out.println("Hallo I'm the PeerAgent! My name is " + getAID().getName());
		
		super.setup();
	}

	protected void takeDown() { // Deregister from the yellow pages try {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("PeerAgent " + getAID().getName() + " sais good bye");
	}

	protected SearchResponse searchForFingerPrint(Fingerprint fingerprint) {
		SearchResponse response = new SearchResponse();
		
		SongManager songManager = new SongManager();
		if (!songManager.searchSongByFingerprint(fingerprint).isEmpty()) {
			Song song =  songManager.searchSongByFingerprint(fingerprint).get(0);
			response.setTitle(song.getTitle());
			response.setTitle(song.getAlbum());
			response.setTitle(song.getArtist());
			response.setYear(song.getYear());
		}
		
		return response;
	}

	private class requestReceiver extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		void reportFailure(ACLMessage request, String text) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent(text);
			myAgent.send(reply);
		}

		@Override
		public void action() {
			ACLMessage requestMessage = myAgent.receive();
			if (requestMessage != null) {
				log("got request!");
				
				try {
					
					SearchRequest request = (SearchRequest) Utility.fromString(requestMessage.getContent());
					SearchResponse response = searchForFingerPrint(request.getFingerprint());
					ACLMessage reply = requestMessage.createReply();
					response.setSearchRequest(request);
					reply.setContent(response.serialize());
					
					request.decrementTimeToLive();
					if (response.wasFound()) {
						log("music found!");
						reply.setPerformative(ACLMessage.CONFIRM);
						myAgent.send(reply);
					} else {
						log("no music found :/");
						
						if(request.getTimeToLive() > 0){ // search request timed out (too many hops) -> create empty response and send it back to the server
							log("forwarding request. timeToLive: "+request.getTimeToLive());
							addRequestForForwarding(request);
						}else{
							log("timed out (timeToLive = 0). notifying client...");
							reply.setPerformative(ACLMessage.CONFIRM);
							myAgent.send(reply);
						}
						//reportFailure(requestMessage, "No links found");
					}
				} catch (IOException e) {
					reportFailure(requestMessage, "Connection Error");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 

			} else {
				block();
			}

		}

		

	}
}
