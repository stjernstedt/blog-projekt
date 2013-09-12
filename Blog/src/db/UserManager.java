package db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import data.User;

public class UserManager {

	private static DatabaseConnection connection;
	private static UserManager userManager = new UserManager();
	
	private static Logger logg = Logger.getLogger("userManager");

	private UserManager() {
		connection = DatabaseConnection.getInstance();
	}

	public static UserManager getInstance() {
		return userManager;
	}

	public User createUser(User user) {
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();

		try {
			em.persist(user);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}

		return user;
	}

	public List<User> getUsers() {
		EntityManager em = connection.getEntityManager();
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

	public User getUser(String name) {
		EntityManager em = connection.getEntityManager();
		User user = null;
		
		logg.info("getuser: "+name);
		em.getTransaction().begin();
		try {
			TypedQuery<User> q = em.createQuery(
					"SELECT user FROM User user WHERE user.username = :name",
					User.class);
			user = q.setParameter("name", name).getSingleResult();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
			logg.info("User: "+user);
		}
		return user;
	}
}
