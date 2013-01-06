package at.tuwien.sa.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SUBFINGERPRINT")
public class SubFingerprint {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SUBFINGERPRINT_ID")
	private Long id;
	
	@Column(name = "VALUE")
	private Integer value;

    @ManyToMany(
    		cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    		mappedBy = "subfingerprints"
    )
    private List<Fingerprint> fingerprints;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public List<Fingerprint> getFingerprints() {
		return fingerprints;
	}

	public void setFingerprints(List<Fingerprint> fingerprints) {
		this.fingerprints = fingerprints;
	}
}
