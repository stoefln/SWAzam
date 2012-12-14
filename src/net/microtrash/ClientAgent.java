package net.microtrash;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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

public class ClientAgent extends Agent {

	private static final long serialVersionUID = 2L;
	private DFAgentDescription server;
	private ResponseReceiver responseReceiver;
	private String searchFingerprint = "";

	protected void log(String message) {
		System.out.println(message);
	}

	@Override
	protected void setup() {
		System.out.println("Hallo I'm a ClientAgent!! My name is " + getAID().getName());

		Object[] args = getArguments();
		if (args != null && args.length != 0) {
			if (args.length > 0) {

			}
			if (args.length > 1 && args[1].equals("stayOnDomain")) {

			}
		} else {
			// doDelete();
		}

		// 1) look for an agent which has registered as "SWAzamServer" every 10
		// seconds (= wait till a server is online)
		addBehaviour(new TickerBehaviour(this, 10000) {

			private static final long serialVersionUID = 1L;

			protected void onTick() {
				if (server == null) {
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("SWAzamServer");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template);
						if (result.length > 0) {
							log("server found: " + result[0].getName());
							server = result[0];

							// TODO: remove this line, this is just a test:
							searchByFingerprint("testFingerprint");
						} else {
							log("waiting for SWAzamServer...");
						}
					} catch (FIPAException fe) {
						fe.printStackTrace();
					}
				}
			}
		});

		// 2) create receiver which takes care of receiving searchResponses,
		responseReceiver = new ResponseReceiver();
		addBehaviour(responseReceiver);

		super.setup();
	}

	@Override
	protected void takeDown() {
		log("ClientAgent " + getAID().getName() + " sais good bye");
	}

	/**
	 * call this from the GUI
	 * 
	 * @param fingerprint
	 */
	public void searchByFingerprint(String fingerprint) {
		this.searchFingerprint = fingerprint;

		// 2) sends a request to the server
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 22L;

			@Override
			public void action() {

				if (server != null) {
					ACLMessage message = new ACLMessage(ACLMessage.CFP);
					message.addReceiver(server.getName());
					message.setContent(searchFingerprint);
					message.setConversationId("search-fingerprint");
					message.setReplyWith("message_" + myAgent.getName() + "_" + System.currentTimeMillis());
					message.setPerformative(ACLMessage.REQUEST);
					myAgent.send(message);
					log("Search request with fingerprint \"" + searchFingerprint + "\" sent to server "
							+ server.getName());
				} else {
					// TODO: output message in GUI
					log("No server connected...");
				}
			}
		});
	}

	private class ResponseReceiver extends CyclicBehaviour {

		private static final long serialVersionUID = 2L;

		@Override
		public void action() {
			ACLMessage reply = myAgent.receive();

			if (reply != null) {
				AID aid = reply.getSender();

				if (reply.getPerformative() == ACLMessage.CONFIRM) { // music
																		// found
					String searchResponseSerialised = reply.getContent();
					SearchResponse searchResponse = null;
					try {
						searchResponse = (SearchResponse) Utility.fromString(searchResponseSerialised);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					// TODO: display searchResponse in GUI

					if (searchResponse.wasFound()) {
						log("search response found! " + searchResponse.toString());
					} else {
						log("not response found (timed out)");
					}

				} else {
					// TODO: display message in GUI
					log("something bad happened. reason: " + reply.getContent());
				}
			} else {
				block();
			}

		}

	}
}
