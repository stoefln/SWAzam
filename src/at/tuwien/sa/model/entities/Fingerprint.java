package at.tuwien.sa.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FINGERPRINT")
public class Fingerprint {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FINGERPRINT_ID")
	private Long id;
	
	@Column(name = "START_TIME")
    private Double startTime;
    
	@Column(name = "SHIFT_DURATION")
	private Double shiftDuration;
	
	@OneToOne(mappedBy = "fingerprint", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Song song;
	
    @ManyToMany
    @JoinTable(
        name = "FINGERPRINT_SUBFINGERPRINT",
        joinColumns = {@JoinColumn(name = "FINGERPRINT_ID")},
        inverseJoinColumns={@JoinColumn(name="SUBFINGERPRINT_ID")}
    )
    private List<SubFingerprint> subfingerprints;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getStartTime() {
		return startTime;
	}

	public void setStartTime(Double startTime) {
		this.startTime = startTime;
	}

	public Double getShiftDuration() {
		return shiftDuration;
	}

	public void setShiftDuration(Double shiftDuration) {
		this.shiftDuration = shiftDuration;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public List<SubFingerprint> getSubfingerprints() {
		return subfingerprints;
	}

	public void setSubfingerprints(List<SubFingerprint> subfingerprints) {
		this.subfingerprints = subfingerprints;
	}
}
