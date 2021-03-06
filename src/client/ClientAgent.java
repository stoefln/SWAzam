package client;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import lib.entities.SearchRequest;
import lib.entities.SearchResponse;
import lib.utils.Utility;
import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;

public class ClientAgent extends GuiAgent {

	private static final long serialVersionUID = 2L;
	private DFAgentDescription server;
	private ResponseReceiver responseReceiver;

	//private ClientGUI clientgui;
	private String mp3Filename = null;
	private String accessToken = null;

	
	private SwazamGUI gui;


	protected void log(String message) {
		System.out.println(message);
	}

	@Override
	protected void setup() {
		System.out.println("Hallo I'm a ClientAgent!! My name is " + getAID().getName());
		
		Object[] args = getArguments();
		if(args != null){
			System.out.println("params:"+args.length);
		}
		if (args != null && args.length != 0) {
			if (args.length > 0 && args[0].equals("gui")) {
				gui = new SwazamGUI(this);
			} else if(args.length > 1 && args[0].equals("cli")){
				mp3Filename  = args[1].toString();
				System.out.println("Search by CLI for "+mp3Filename+". Hold on...");
				
				try {
					byte[] audio = Utility.fileToByteArray(mp3Filename);
					FingerprintSystem fs = new FingerprintSystem(22000);
					Fingerprint fingerprint = fs.fingerprint(audio);
				
					searchByFingerPrint(fingerprint);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			gui = new SwazamGUI(this);
		}

		// 1) look for an agent which has registered as "SWAzamServer" every second (= wait till a server is online)
		addBehaviour(new TickerBehaviour(this, 1000) {

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
	 * @param fingerPrint
	 */
	public void searchByFingerPrint(final Fingerprint fingerPrint) {
	
		
		// 2) sends a request to the server
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 22L;

			@Override
			public void action() {

				if (server != null) {
					
					SearchRequest request = new SearchRequest(fingerPrint, myAgent.getAID());
					request.setAcessToken(accessToken);
					ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
					message.addReceiver(server.getName());
					message.setContent(request.serialize());
					message.setConversationId("search-fingerPrint");
					message.setReplyWith("message_" + myAgent.getName() + "_"
							+ System.currentTimeMillis());
					myAgent.send(message);
					log("Search request with fingerPrint for \""
							+ mp3Filename + "\" sent to server "
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

					gui.setResult(searchResponse);
					if (searchResponse.wasFound()) {
						log("search response found! ");
						log(searchResponse.toString());
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

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		if(ev.getType()==EventTypes.SESSION_TOKEN){
			accessToken = (String) ev.getParameter(0);
		}else
		if(ev.getType()==EventTypes.BROWSE){
			String fileName = (String) ev.getParameter(0); 
			mp3Filename = fileName;
		}
		else
			if(ev.getType()==EventTypes.SEND){
				log("generating fingerprint. hold on...");
				String fileName = (String) ev.getParameter(0); 
				mp3Filename = fileName;
				try {
					byte[] audio = Utility.fileToByteArray(mp3Filename);
					FingerprintSystem fs = new FingerprintSystem(22000);
					Fingerprint fingerprint = fs.fingerprint(audio);
					log("fingerprint generated.");
					searchByFingerPrint(fingerprint); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
}
