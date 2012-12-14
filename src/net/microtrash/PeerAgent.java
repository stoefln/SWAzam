package net.microtrash;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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

		System.out.println("PeerAgent "+getAID().getName()+" sais good bye");
	}

	protected SearchResponse searchForFingerPrint(String fingerPrint){
		//TODO: replace next line with implementation (its just for testing)
		return new SearchResponse(fingerPrint);
	}
	
	private class requestReceiver extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		
		void reportFailure(ACLMessage request, String text) {
			log("reporting back: " + text);
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent(text);
			myAgent.send(reply);
		}

		
		
		@Override
		public void action() {
			log("requestReceiver action()");
			ACLMessage request = myAgent.receive(); 
			if (request != null) {
				log("got message!");
				String fingerPrint = request.getContent();

				try {
					
					SearchResponse searchResponse = searchForFingerPrint(fingerPrint);
					ACLMessage reply = request.createReply();
					if(searchResponse != null){
						String serialisedSearchResponse = Utility.toString(searchResponse);
						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContent(serialisedSearchResponse);
						myAgent.send(reply);
					} else {
						
						//TODO: implement forwarding of request to other peers here
						
						reportFailure(request, "No links found");
					}
				
				} catch (IOException e) {
					reportFailure(request, "Connection Error");
				} catch(IllegalArgumentException e){
					reportFailure(request, "Malformed URL: "+fingerPrint);
				}
				
			} else {
				log("responseReceiver block()");
				block();
			}
			
		}
		
	}
}
