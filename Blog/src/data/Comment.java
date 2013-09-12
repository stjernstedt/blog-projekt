package data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private int commentID;
	private String email;
	private String text;
	private String name;
	private Date date;
	private int userID;
	
	public Comment() {
		
	}

	public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getUserId() {
		return userID;
	}
	
	public void setUserId(int userID) {
		this.userID = userID;
	}
	
}
