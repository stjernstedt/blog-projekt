package data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
@Entity
public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private int commentId;
	private String email;
	private String text;
	private String name;
	private Date date;
	private int postId;
	
	public Comment() {
		
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
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
	
	public int getPostId() {
		return postId;
	}
	
	public void setPostId(int postId) {
		this.postId = postId;	}
	
}
