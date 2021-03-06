package data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
@Entity
public class Session implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String sessID = UUID.randomUUID().toString();
	private String username;
	private int userType;
	private int userId;
	private Calendar lastUse = Calendar.getInstance();

	public Session() {
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessID() {
		return sessID;
	}

	public void setLastUse(Date date) {
		this.lastUse.setTime(date);
	}
	
	public Date getlastUse() {
		return lastUse.getTime();
	}
	
	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	public int getUserType() {
		return userType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
