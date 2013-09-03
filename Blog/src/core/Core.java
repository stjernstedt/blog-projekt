package core;

import java.util.ArrayList;
import java.util.List;

import data.User;
import db.UserManager;

public class Core {
	
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
	
	public void login() {
		
	}
	
	public void logout() {
		
	}
	
	public void createPost() {
		
	}
	
	public void removePost() {
		
	}
	
}
