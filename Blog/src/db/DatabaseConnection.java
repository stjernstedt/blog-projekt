package db;

import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;

public class DatabaseConnection extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static EntityManagerFactory emf;
	private static Logger logg = Logger.getLogger("databaseConnection");

	public static EntityManager getEntityManager() {
		MessageContext mc = MessageContext.getCurrentMessageContext();
		ServletContext sc = (ServletContext)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
		
		emf = (EntityManagerFactory)sc.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		return em;
	}

}
