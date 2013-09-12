package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import data.Comment;
import data.Post;
import data.Session;
import data.User;
import db.CommentManager;
import db.PostManager;
import db.UserManager;

public class Core {

	private Logger logg = Logger.getLogger("Core Logger");

	public String test(String s1) {
		String result = s1 + " success";
		return result;
	}

	public void createUser(String username, String password, String email,
			int usertype) {
		User user = new User();
		UserManager userManager = UserManager.getInstance();

		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setUsertype(usertype);

		userManager.createUser(user);
	}

	public void removeUser(int id) {

	}

	public List<User> getUsers() {
		UserManager userManager = UserManager.getInstance();

		List<User> result = new ArrayList<User>();

		result = userManager.getUsers();
		result.toArray();

		return result;
	}

	public UUID login(String username, String password) {
		UserManager userManager = UserManager.getInstance();
		
		User user = userManager.getUser(username);
		if(user.getPassword() == password) {
			Session session = new Session();
			UUID sessID = session.getSessID();
			return sessID;
		}
		
		return null;
	}

	public void logout(long sessionsId) {

	}

	public void createPost(String title, String text) {
		Post post = new Post();
		PostManager postManager = PostManager.getInstance();
		
		post.setTitle(title);
		//post.setDate(date);
		post.setText(text);
		//post.setUserId(userId);
		
		postManager.createPost(post);
		

	}

	public void removePost(long sessionId, Post post) {

	}
	
	public void createComment(String email, String text, String name, Date date, int userID) {
		Comment comment = new Comment();
		CommentManager commentManager = CommentManager.getInstance();
		
		comment.setEmail(email);
		comment.setName(name);
		comment.setText(text);
		comment.setUserId(userID);
		
		commentManager.createComment(comment);
	}

}
