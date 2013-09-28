package managers;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import core.CoreStub;
import core.CoreStub.GetSession;
import core.CoreStub.GetSessionResponse;
import core.CoreStub.Session;
import core.CoreStub.User;

public class SessionManager {
	
	public Session getSession(CoreStub server, String session) {
		GetSession arg = new GetSession();
		GetSessionResponse result = null;
		
		arg.setSession(session);
		try {
			result = server.getSession(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		
		return result.get_return();
	}
}
