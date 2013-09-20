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

	// skapar en användare
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
	
	// ändrar en användare
	public void editUser() {
		//TODO edit user metod
	}

	// tar bort en användare
	public void removeUser(int userId) {
		UserManager userManager = UserManager.getInstance();
		userManager.removeUser(userId);
	}

	// hämtar alla användare
	public List<User> getUsers() {
		UserManager userManager = UserManager.getInstance();

		List<User> result = new ArrayList<User>();

		result = userManager.getUsers();

		return result;
	}
	
	// hämtar en användare
	public User getUser(int userId) {
		UserManager userManager = UserManager.getInstance();
		
		return userManager.getUser(userId);
	}

	// hämtar alla inlägg
	public List<Post> getPosts() {
		PostManager postManager = PostManager.getInstance();

		List<Post> result = new ArrayList<Post>();

		result = postManager.getPosts();

		return result;
	}

	// login metod
	public String login(String username, String password) {
		UserManager userManager = UserManager.getInstance();

		logg.info(username + " " + password);
		User user = userManager.searchUser(username);
		logg.info("stored password: " + user.getPassword()
				+ "  user password: " + password);
		if (user.getPassword().equals(password)) {
			Session session = new Session();
			UUID sessID = session.getSessID();
			return sessID.toString();
		} else {
			return "error";
		}
	}

	public void logout(long sessionsId) {

	}

	// skapar ett inlägg
	public void createPost(String title, String text) {
		Post post = new Post();
		PostManager postManager = PostManager.getInstance();

		post.setTitle(title);
		// post.setDate(date);
		post.setText(text);
		// post.setUserId(userId);

		postManager.createPost(post);

	}

	public void removePost(long sessionId, Post post) {

	}

	// skapar en kommentar
	public void createComment(String email, String text, String name,
			Date date, int userID) {
		Comment comment = new Comment();
		CommentManager commentManager = CommentManager.getInstance();

		comment.setEmail(email);
		comment.setName(name);
		comment.setText(text);
		comment.setUserId(userID);

		commentManager.createComment(comment);
	}

}
