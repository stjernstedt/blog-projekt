package core;

import java.util.List;

import data.Post;
import data.User;
import db.UserManager;

public class Core {
	
	public String test(String s1) {
		String result = s1+" success";
		return result;
	}
	
	public void createUser(String username, String password, String email,int usertype) {
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
		
		return userManager.getUsers();
	}
	
	public long login(String userName, String Password) {
		return 0;
		
	}
	
	public void logout(long sessionsId) {
		
	}
	
	public Post createPost(long sessionsId) {
		return null;
		
	}
	
	public void removePost(long sessionId, Post post) {
		
	}
	
}
