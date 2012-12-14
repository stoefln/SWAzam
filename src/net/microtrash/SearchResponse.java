package net.microtrash;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class SearchResponse implements Serializable {

	private static final long serialVersionUID = 2322L;
	private String fingerPrint;
	private String title;
	private int year;
	private String artist;
	private String album;
	private SearchRequest searchRequest;
	
	public SearchResponse(String fingerPrint){
		this.fingerPrint = fingerPrint;
	}
	public String getFingerPrint() {
		return fingerPrint;
	}
	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int i) {
		this.year = i;
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
				"year:        " + year + "\n" +
				"fingerPrint: " + fingerPrint + "\n"; 
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
