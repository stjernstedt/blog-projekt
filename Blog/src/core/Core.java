package core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import data.Comment;
import data.Post;
import data.Session;
import data.User;
import db.CommentManager;
import db.PostManager;
import db.SessionManager;
import db.UserManager;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
public class Core {

	private Logger logg = Logger.getLogger("Core Logger");
	private UserManager um = UserManager.getInstance();
	private PostManager pm = PostManager.getInstance();
	private SessionManager sm = SessionManager.getInstance();
	private Calendar calendar = Calendar.getInstance();
	private CommentManager cm = CommentManager.getInstance();

	// login metod
	public String login(String username, String password) {
		User user = um.searchUser(username);
		logg.info("user found: " + user);
		if (user != null && user.getPassword().equals(password)) {
			Session session = new Session();
			String sessID = session.getSessID();
			session.setUserType(user.getUsertype());
			session.setUsername(user.getUsername());
			session.setLastUse(calendar.getTime());
			session.setUserId(user.getUserId());
			sm.createSession(session);
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

		um.createUser(user);
	}

	// ändrar en användare
	public void editUser(int userId, String username, String password,
			String email, int usertype) {
		um.editUser(userId, username, password, email, usertype);
	}

	// tar bort en användare
	public void removeUser(int userId) {
		um.removeUser(userId);
	}

	// hämtar alla användare
	public List<User> getUsers() {
		List<User> result = new ArrayList<User>();

		result = um.getUsers();

		if (result.isEmpty()) {
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			user.setEmail("admin@admin");
			user.setUsertype(0);
			um.createUser(user);
			result = um.getUsers();
		}

		return result;
	}

	// hämtar en användare
	public User getUser(int userId) {
		return um.getUser(userId);
	}

	// söker efter användare via användarnamn
	public User searchUser(String username) {
		return um.searchUser(username);
	}

	// skapar ett inlägg
	public void createPost(String title, String text, String username) {
		Post post = new Post();

		post.setTitle(title);
		post.setDate(calendar.getTime());
		post.setText(text);
		post.setUsername(username);

		pm.createPost(post);

	}

	// ändrar ett inlägg
	public void editPost(int postId, String title, String text) {
		pm.editPost(postId, title, text);
	}

	// tar bort ett inlägg
	public void removePost(int postId) {
		pm.removePost(postId);
	}

	// hämtar ett inlägg
	public Post getPost(int postId) {
		return pm.getPost(postId);
	}

	// hämtar alla inlägg
	public List<Post> getPosts(String sessionKey) {
		logg.info("får getPosts anrop med session: " + sessionKey);
		List<Post> result = new ArrayList<Post>();

		Session session = sm.getSession(sessionKey);
		result = pm.getPosts(session.getUsername(), session.getUserType());

		if (result.isEmpty()) {
			Post post = new Post();
			post.setDate(calendar.getTime());
			post.setTitle("Hello World");
			post.setText("Welcome to your new blog!");
			post.setUsername("admin");
			pm.createPost(post);
			// result = pm.getPosts(session.getUserId(), session.getUserType());
			result.add(post);
		}

		return result;
	}

	// skapar en kommentar
	public void createComment(String email, String text, String name, int postId) {
		Comment comment = new Comment();

		comment.setEmail(email);
		comment.setName(name);
		comment.setText(text);
		comment.setPostId(postId);
		comment.setDate(calendar.getTime());

		cm.createComment(comment);
	}

	// tar bort en kommentar
	public void removeComment(int commentID) {
		cm.removeComment(commentID);
	}
	
	public List<Comment> getComments() {
		List<Comment> result = new ArrayList<Comment>();

		result = cm.getComments();

		if (result.isEmpty()) {
			Comment comment = new Comment();
			comment.setDate(calendar.getTime());
			comment.setName("admin");
			comment.setPostId(0);
			comment.setEmail("email");
			comment.setText("Your first comment!");
			cm.createComment(comment);
			result = cm.getComments();
		}

		return result;
	}
	
	public List<Comment> getPostComments(int postId) {
		List<Comment> result = new ArrayList<Comment>();
		
		result = cm.getPostComments(postId);
		
//		if (result.isEmpty())
//			return null;
		
		return result;
	}

	// hämtar en session via sessionkey
	public Session getSession(String session) {
		logg.info("får getSession anrop");
		return sm.getSession(session);
	}
}
