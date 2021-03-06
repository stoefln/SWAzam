package lib.entities;

import java.io.IOException;
import java.io.Serializable;

import lib.utils.Utility;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class SearchResponse implements Serializable {

	private static final long serialVersionUID = 2322L;
	private String title = "";
	private long year = 0;
	private String artist = "";
	private String album = "";
	private SearchRequest searchRequest = null;
	private String respondentToken = "";
	

	public void setRespondentToken(String token) {
		this.respondentToken = token;
	}
	public String getRespondentToken() {
		return respondentToken;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getYear() {
		return year;
	}
	public void setYear(long year) {
		this.year = year;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public boolean wasFound() {
		return this.title != null;
	}
	public void setSearchRequest(SearchRequest request) {
		this.searchRequest = request;
	}
	public SearchRequest getSearchRequest() {
		return this.searchRequest;
	}
	public String toString(){
		return  "title:       " + title + "\n" +
				"artist:      " + artist + "\n" + 
				"album:       " + album + "\n" +
				"year:        " + year + "\n";
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
