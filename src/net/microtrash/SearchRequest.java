package net.microtrash;

import java.io.IOException;
import java.io.Serializable;

import jade.core.AID;

public class SearchRequest implements Serializable {

	private String fingerPrint;
	private String accessToken;
	/**
	 * agent id of the client which initiated the search
	 */
	private AID initiator;

	public String getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(String fingerPrint) {
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

	public SearchRequest(String searchFingerPrint, AID initiator) {
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
