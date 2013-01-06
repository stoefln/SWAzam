package at.tuwien.sa.model.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SONG")
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SONG_ID")
	private Long id;

	@Column(name = "TITLE", nullable = false)
	private String title;
	
	@Column(name = "ARTIST", nullable = false)
	private String artist;
	
	@Column(name = "ALBUM", nullable = false)
	private String album;
	
	@Column(name = "YEAR", nullable = false, length = 4)
	private Long year;
	
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "FINGERPINT_ID")
    private Fingerprint fingerprint;
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}
	
	public Fingerprint getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(Fingerprint fingerprint) {
		this.fingerprint = fingerprint;
	}
}
