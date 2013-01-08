package client;

import jade.util.leap.Serializable;
import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Request implements Serializable{
	
	/**
	 * 
	 */
	private  final long serialVersionUID = 1L;
	private  Fingerprint fingerPrint;
	private  String accessToken;
	private String UID;
	private String createdOn;
	private String timeToLive;
	
	public Request() {
		
	}
	public Request(Fingerprint fp, String token) {
		this.fingerPrint = fp; 
		this.accessToken = token; 
	}

	public void setFingerPrint(Fingerprint fingerPrint) {
		this.fingerPrint = fingerPrint;
	}
	
	public  Fingerprint getFingerPrint() {
		return fingerPrint;
	}
	
	public   void setAcessToken(String acessToken) {
		accessToken = acessToken;
	}
	
	public  String getAcessToken() {
		return accessToken;
	}

	public void setUID(String UID) {
		this.accessToken = UID;
	}
	
	public  boolean isAcessTokenSet(){	
		
		if(accessToken.length()!=0)
			return true;
		
		return false;
	}

	public String getUID() {
		return UID;
	}
	
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}
	
}