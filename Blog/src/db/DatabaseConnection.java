package db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseConnection {
	
	public static DatabaseConnection singleDatabaseConnection = null;
	public EntityManagerFactory emf = null;
	
	public DatabaseConnection() {
		
	}
	
	public static DatabaseConnection getInstance() {
		if (singleDatabaseConnection == null) {
			singleDatabaseConnection = new DatabaseConnection();
		}
		return singleDatabaseConnection;
	}
	
	public EntityManager getEntityManager() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("$objectdb/db/database.odb");
		}
		return emf.createEntityManager();
	}
	
}
