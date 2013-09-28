package db;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import data.Session;

public class SessionManager {

	private static SessionManager sessionManager = new SessionManager();
	private static Logger logg = Logger.getLogger("Session Manager");

	private SessionManager() {

	}

	public static SessionManager getInstance() {
		return sessionManager;
	}

	public void createSession(Session session) {
		EntityManager em = DatabaseConnection.getEntityManager();

		logg.info("försöker spara session: "+ session);
		em.getTransaction().begin();
		try {
			em.persist(session);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

	public Session getSession(String sessionKey) {
		EntityManager em = DatabaseConnection.getEntityManager();
		Session session = null;
		
		logg.info("söker efter session: "+sessionKey);
		em.getTransaction().begin();
		try {
			TypedQuery<Session> q = em
					.createQuery(
							"SELECT session FROM Session session WHERE session.sessID = :search",
							Session.class);
			session = q.setParameter("search", sessionKey).getSingleResult();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		logg.info("hittat session med sessionKey: "+session);
		return session;
	}
}
