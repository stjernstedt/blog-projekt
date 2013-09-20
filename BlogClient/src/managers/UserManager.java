package managers;

import java.rmi.RemoteException;

import core.CoreStub;
import core.CoreStub.CreateUser;
import core.CoreStub.GetUser;
import core.CoreStub.GetUserResponse;
import core.CoreStub.GetUsers;
import core.CoreStub.GetUsersResponse;
import core.CoreStub.User;

public class UserManager {

	public User[] getUsers(CoreStub server) {
		GetUsers arg = new GetUsers();
		GetUsersResponse result = null;

		try {
			result = server.getUsers(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}

		return result.get_return();
	}
	
	public User getUser(CoreStub server, int userId) {
		GetUser arg = new GetUser();
		GetUserResponse result = null;
		
		arg.setUserId(userId);
		
		try {
			result = server.getUser(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		
		return result.get_return();
	}

	public void createUser(CoreStub server, String username, String password,
			String email, int usertype) {
		CreateUser user = new CreateUser();
		user.setEmail(email);
		user.setPassword(password);
		user.setUsername(username);
		user.setUsertype(usertype);

		try {
			server.createUser(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
