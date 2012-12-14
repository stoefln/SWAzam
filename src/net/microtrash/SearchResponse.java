package net.microtrash;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResponse implements Serializable {

	private static final long serialVersionUID = 2322L;
	private String fingerPrint;
	private String title;
	private String year;
	private String artist;
	private String album;
	
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
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
	
}
