package core;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application Lifecycle Listener implementation class DatabaseSetup
 * 
 */
public class DatabaseSetup implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public DatabaseSetup() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		com.objectdb.Enhancer.enhance("data.*");
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("$objectdb/db/database.odb");
		arg0.getServletContext().setAttribute("emf", emf);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		EntityManagerFactory emf = (EntityManagerFactory) arg0
				.getServletContext().getAttribute("emf");
		if (emf != null)
			emf.close();
	}

}
