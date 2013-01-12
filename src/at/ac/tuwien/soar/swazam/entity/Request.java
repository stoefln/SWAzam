package at.ac.tuwien.soar.swazam.entity;

// Generated 04.01.2013 18:09:12 by Hibernate Tools 4.0.0

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

import net.microtrash.SearchResponse;

import at.tuwien.sa.model.entities.Fingerprint;

/**
 * Request generated by hbm2java
 */
@Entity
@Table(name = "request", schema = "public")
public class Request implements java.io.Serializable {

	private int id;
	private User userBySenderId;
	private User userBySolverId;
	private Date created;
	private Date resent;
	private Date solved;
	private Fingerprint fingerprint;
	private SearchResponse response;

	public void setId(int id) {
		this.id = id;
	}

	public Request() {
	}

	public Request(int id, User userBySenderId, Date created, Fingerprint fingerprint) {
		this.id = id;
		this.userBySenderId = userBySenderId;
		this.created = created;
		this.fingerprint = fingerprint;
	}

	public Request(int id, User userBySenderId, User userBySolverId,
			Date created, Date resent, Date solved, Fingerprint fingerprint,
			SearchResponse response) {
		this.id = id;
		this.userBySenderId = userBySenderId;
		this.userBySolverId = userBySolverId;
		this.created = created;
		this.resent = resent;
		this.solved = solved;
		this.fingerprint = fingerprint;
		this.response = response;
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

	@Column(name = "response")
	public SearchResponse getResponse() {
		return this.response;
	}

	public void setResponse(SearchResponse response) {
		this.response = response;
	}

}
