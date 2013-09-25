package db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import data.User;

public class UserManager {

	private static UserManager userManager = new UserManager();

	private static Logger logg = Logger.getLogger("userManager");

	private UserManager() {
	}

	public static UserManager getInstance() {
		return userManager;
	}

	// sparar en användare i databasen
	public User createUser(User user) {
		EntityManager em = DatabaseConnection.getEntityManager();
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

	// ändrar en användare
	public void editUser(int userId, String username, String password,
			String email, int usertype) {
		EntityManager em = DatabaseConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			User user = em.find(User.class, userId);
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
			user.setUsertype(usertype);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

	// hämtar alla användare från databasen
	public List<User> getUsers() {
		EntityManager em = DatabaseConnection.getEntityManager();
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

	// hämtar en specifik användare
	public User getUser(int userId) {
		EntityManager em = DatabaseConnection.getEntityManager();
		User user = null;

		em.getTransaction().begin();
		try {
			user = em.find(User.class, userId);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return user;
	}

	// söker efter en användare via användarnamn
	public User searchUser(String search) {
		EntityManager em = DatabaseConnection.getEntityManager();
		User user = null;

		em.getTransaction().begin();
		try {
			TypedQuery<User> q = em.createQuery(
					"SELECT user FROM User user WHERE user.username = :search",
					User.class);
			user = q.setParameter("search", search).getSingleResult();
			em.getTransaction().commit();
		} catch (NoResultException e) {
			
		} finally {
		if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		logg.info(user);
		return user;
	}

	// tar bort en användare
	public void removeUser(int userId) {
		EntityManager em = DatabaseConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			User user = em.find(User.class, userId);
			em.remove(user);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}
}
