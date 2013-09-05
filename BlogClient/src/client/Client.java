package client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import core.CoreStub;
import core.CoreStub.CreateUser;
import core.CoreStub.Test;
import core.CoreStub.TestResponse;


public class Client {
	
	private CoreStub server = null;

	public static void main(String[] args) {
		
		Client client = new Client();
		client.start();
		System.exit(0);
	}
	
	private Client() {
		server = initializeServer();
	}
	
	/**
	 * Metoden start() är det nya main(String[] args)
	 */
	public void start() {
		String arg = "Apa";
		System.out.println(test(arg));
		
		String username = "test4";
		String password = "test3";
		String email = "test3";
		int usertype = 2;
		
		createUser(username, password, email, usertype);
	}
	
	
	public static CoreStub initializeServer() {
		CoreStub server = null;
		try {
			server = new CoreStub();
		} catch (AxisFault e) {
			System.err.println("Fel vid skapandet av server-stubben");
			e.printStackTrace();
			System.exit(-1);
		}
		return server;		
	}
	
	public String test(String s1) {
		Test arg = new Test();
		arg.setS1(s1);

		TestResponse answer = null;
		try {
			answer = server.test(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		
		return answer.get_return();
	}
	
	public void createUser(String username, String password, String email, int usertype) {
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
