package data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Session implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private UUID sessID = UUID.randomUUID();
	private User user; 
	private int userType;
	private Calendar lastUse = Calendar.getInstance();

	public Session() {
		updateLastUsed();
	}
	
	private void updateLastUsed() {
		lastUse.setTime(Calendar.getInstance().getTime());
	}

	public User getUser() {
		updateLastUsed();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UUID getSessID() {
		return sessID;
	}

	public Calendar lastUse() {
		return lastUse;
	}
	
	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	public int getUserType() {
		return userType;
	}
}
