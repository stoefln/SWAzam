package lib.entities;


import jade.core.AID;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lib.utils.Utility;
import ac.at.tuwien.infosys.swa.audio.Fingerprint;

@Entity
@Table(name = "request", schema = "public")
public class SearchRequest implements java.io.Serializable {
	private static final long serialVersionUID = -2326932267771781244L;

	private int id;
	private User userBySenderId;
	private User userBySolverId;
	private Date created;
	private Date resent;
	private Date solved;
	private Fingerprint fingerprint;
	private String accessToken;
	private AID initiator;
	
	public void setId(int id) {
		this.id = id;
	}

	public SearchRequest() {
	}
	
	public SearchRequest(Fingerprint searchFingerPrint, AID initiator) {
		this.initiator = initiator;
		this.fingerprint = searchFingerPrint;
	}

	public SearchRequest(int id, User userBySenderId, Date created, Fingerprint fingerprint) {
		this.id = id;
		this.userBySenderId = userBySenderId;
		this.created = created;
		this.fingerprint = fingerprint;
	}

	public SearchRequest(int id, User userBySenderId, User userBySolverId,
			Date created, Date resent, Date solved, Fingerprint fingerprint,
			SearchResponse response) {
		this.id = id;
		this.userBySenderId = userBySenderId;
		this.userBySolverId = userBySolverId;
		this.created = created;
		this.resent = resent;
		this.solved = solved;
		this.fingerprint = fingerprint;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return this.id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	public User getUserBySenderId() {
		return this.userBySenderId;
	}

	public void setUserBySenderId(User userBySenderId) {
		this.userBySenderId = userBySenderId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "solver_id")
	public User getUserBySolverId() {
		return this.userBySolverId;
	}

	public void setUserBySolverId(User userBySolverId) {
		this.userBySolverId = userBySolverId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 29)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "resent", length = 29)
	public Date getResent() {
		return this.resent;
	}

	public void setResent(Date resent) {
		this.resent = resent;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "solved", length = 29)
	public Date getSolved() {
		return this.solved;
	}

	public void setSolved(Date solved) {
		this.solved = solved;
	}

	@Column(name = "fingerprint", nullable = false)
	public Fingerprint getFingerprint() {
		return this.fingerprint;
	}

	public void setFingerprint(Fingerprint fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String serialize() {
		try {
			return Utility.toString(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setAcessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Transient
	public boolean isAcessTokenSet() {
		return accessToken != null;
	}

	@Transient
	public AID getInitiator() {		
		return this.initiator;
	}
	
}
