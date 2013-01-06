package net.microtrash;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import at.tuwien.sa.fingerprinting.SongManager;
import at.tuwien.sa.model.entities.Song;

public class PeerAgent extends Agent {

	/**
	 * 
	 */
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
		// TODO: replace next lines with implementation (its just for testing)
		SearchResponse response = new SearchResponse(fingerprint);
		
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
					SearchResponse searchResponse = searchForFingerPrint(request.getFingerPrint());
					searchResponse.setSearchRequest(request);
					
					ACLMessage reply = requestMessage.createReply();
					if (searchResponse.wasFound()) {
						log("music found!");
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContent(searchResponse.serialize());
						myAgent.send(reply);
					} else {
						log("no music found :/");
						// TODO: implement forwarding of request to other peers
						// here

						reportFailure(requestMessage, "No links found");
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
