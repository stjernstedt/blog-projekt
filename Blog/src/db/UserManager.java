package db;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import data.User;

public class UserManager {

	private static DatabaseConnection connection;
	private static UserManager userManager = new UserManager();

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

	public User getUser(String username) {
		EntityManager em = connection.getEntityManager();
		User user;

		em.getTransaction().begin();
		try {
			TypedQuery<User> q = em.createQuery(
					"SELECT User FROM User user WHERE username = :username",
					User.class);
			user = q.getSingleResult();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return user;
	}
}
