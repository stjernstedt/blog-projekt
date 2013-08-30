package core;

import data.User;

public class Users {
	
	public void createUser(String username, String password, String email,int usertype) {
		User user = new User();
		
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setUsertype(usertype);
	}
	
	public void removeUser(int id) {
		
	}
	
	public void login() {
		
	}
	
	public void logout() {
		
	}
	
}
