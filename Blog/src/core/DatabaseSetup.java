package core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import data.User;
import db.DatabaseConnection;

/**
 * Application Lifecycle Listener implementation class DatabaseSetup
 * 
 */
public class DatabaseSetup implements ServletContextListener {

	EntityManagerFactory emf;
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
		emf = Persistence
				.createEntityManagerFactory("$objectdb/db/database.odb");
		arg0.getServletContext().setAttribute("emf", emf);
		
		List<User> result = new ArrayList<User>();
		result = getUsers();
		if (result.isEmpty()) {
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			user.setEmail("admin@admin");
			user.setUsertype(0);
			createUser(user);
		}
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
	
	public void createUser(User user) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		try {
			em.persist(user);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}
	
	public List<User> getUsers() {
		EntityManager em = emf.createEntityManager();
		List<User> allUsers = new ArrayList<User>();

		em.getTransaction().begin();
		try {
			TypedQuery<User> q = em.createQuery("SELECT User FROM User user",
					User.class);

			allUsers = q.getResultList();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return allUsers;
	}

}
