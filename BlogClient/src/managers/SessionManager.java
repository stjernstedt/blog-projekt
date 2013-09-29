package managers;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import core.CoreStub;
import core.CoreStub.GetSession;
import core.CoreStub.GetSessionResponse;
import core.CoreStub.Session;

public class SessionManager {

	private static Logger logg = Logger.getLogger("sessionManager");

	public Session getSession(CoreStub server, String session) {
		logg.info("får getSession anrop med session: " + session);
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
