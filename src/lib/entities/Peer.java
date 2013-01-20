package lib.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PEER", schema = "public")
public class Peer implements java.io.Serializable {
	private static final long serialVersionUID = -3667935919922988742L;
	
	private int id;
	private User user;
	private String ip;
	private int port;

	public Peer() {
	}

	public Peer(int id, User user, String ip, int port) {
		this.id = id;
		this.user = user;
		this.ip = ip;
		this.port = port;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "ip", nullable = false, length = 15)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "port", nullable = false)
	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
