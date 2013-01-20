package lib.entities;


import jade.core.AID;

import java.io.IOException;
import java.util.Date;

import lib.utils.Utility;
import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class SearchRequest extends Request implements java.io.Serializable {
	private static final long serialVersionUID = -2326932267771781244L;

	private Fingerprint fingerprint;
	private SearchResponse response;
	private String accessToken;
	private AID initiator;
	

	public SearchRequest() {
	}
	
	public SearchRequest(Fingerprint searchFingerPrint, AID initiator) {
		this.initiator = initiator;
		this.fingerprint = searchFingerPrint;
	}

	public SearchRequest(User userBySenderId, User userBySolverId,
			Date created, Date resent, Date solved, Fingerprint fingerprint,
			SearchResponse response) {
		super(userBySenderId, created);
		this.setUserBySolverId(userBySolverId);
		this.setResent(resent);
		this.setSolved(solved);
		this.setFingerprint(fingerprint);
		this.setResponse(response);
	}

	
	public Fingerprint getFingerprint() {
		return this.fingerprint;
	}

	public void setFingerprint(Fingerprint fingerprint) {
		this.fingerprint = fingerprint;
	}

	public SearchResponse getResponse() {
		return this.response;
	}

	public void setResponse(SearchResponse response) {
		this.response = response;
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

	public void setAcessToken(String accessToken) {
		this.accessToken = accessToken;
		
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public boolean isAcessTokenSet() {
		return accessToken != null;
	}

	public AID getInitiator() {		
		return this.initiator;
	}
	
}
