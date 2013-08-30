package db;

import javax.persistence.EntityManager;

import data.User;

public class UserMapper {
	
	private static DatabaseConnection connection;
	
	private static UserMapper userMapper = new UserMapper();
	
	private UserMapper() {
		connection = DatabaseConnection.getInstance();
	}
	
	public static UserMapper getInstance() {
		return userMapper;
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
