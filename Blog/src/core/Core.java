package core;

import java.util.ArrayList;
import java.util.Calendar;
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
	private UserManager userManager = UserManager.getInstance();
	private Calendar calendar = Calendar.getInstance();

	// login metod
	public String login(String username, String password) {
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

	// skapar en användare
	public void createUser(String username, String password, String email,
			int usertype) {
		User user = new User();

		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setUsertype(usertype);

		userManager.createUser(user);
	}
	
	// ändrar en användare
	public void editUser(int userId, String username, String password, String email, int usertype) {
		userManager.editUser(userId, username, password, email, usertype);
	}

	// tar bort en användare
	public void removeUser(int userId) {
		userManager.removeUser(userId);
	}

	// hämtar alla användare
	public List<User> getUsers() {
		List<User> result = new ArrayList<User>();

		result = userManager.getUsers();

		return result;
	}
	
	// hämtar en användare
	public User getUser(int userId) {
		return userManager.getUser(userId);
	}

	// skapar ett inlägg
	public void createPost(String title, String text) {
		Post post = new Post();
		PostManager postManager = PostManager.getInstance();

		post.setTitle(title);
		post.setDate(calendar.getTime());
		post.setText(text);
		// post.setUserId(userId);

		postManager.createPost(post);

	}

	public void removePost(long sessionId, Post post) {

	}

	// hämtar alla inlägg
	public List<Post> getPosts() {
		PostManager postManager = PostManager.getInstance();

		List<Post> result = new ArrayList<Post>();

		result = postManager.getPosts();

		return result;
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
		comment.setDate(calendar.getTime());

		commentManager.createComment(comment);
	}

}
