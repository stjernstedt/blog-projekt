package data;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Session implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private UUID sessID = UUID.randomUUID();
	private User user; 
	private long timeCreated = System.currentTimeMillis();
	
	public Session() {
		
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UUID getSessID() {
		return sessID;
	}

	public long getTimeCreated() {
		return timeCreated;
	}
	
	
}
