package db;

import javax.persistence.EntityManager;

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
			if(em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
		em.close();
		
		return user;
	}
}
