package core;

import data.User;
import db.UserManager;

public class Core {
	
	public void createUser(String username, String password, String email,int usertype) {
		User user = new User();
		UserManager usermapper = UserManager.getInstance();
		
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setUsertype(usertype);
		
		usermapper.createUser(user);
	}
	
	public void removeUser(int id) {
		
	}
	
	public void login() {
		
	}
	
	public void logout() {
		
	}
	
	public void createPost() {
		
	}
	
	public void removePost() {
		
	}
	
}
