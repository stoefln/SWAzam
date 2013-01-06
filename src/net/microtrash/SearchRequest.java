package net.microtrash;

import jade.core.AID;

import java.io.IOException;
import java.io.Serializable;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class SearchRequest implements Serializable {
	private static final long serialVersionUID = -2338850142504964916L;
	
	private Fingerprint fingerPrint;
	private String accessToken;
	/**
	 * agent id of the client which initiated the search
	 */
	private AID initiator;

	public Fingerprint getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(Fingerprint fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public AID getInitiator() {
		return initiator;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public SearchRequest(Fingerprint searchFingerPrint, AID initiator) {
		this.fingerPrint = searchFingerPrint;
		this.initiator = initiator;
	}

	public String serialize() {
		try {
			return Utility.toString(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
