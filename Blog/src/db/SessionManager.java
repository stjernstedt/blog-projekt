package db;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import data.Session;

public class SessionManager {

	private static SessionManager sessionManager = new SessionManager();

	private SessionManager() {

	}

	public static SessionManager getInstance() {
		return sessionManager;
	}

	public void createSession(Session session) {
		EntityManager em = DatabaseConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			em.persist(session);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

	public Session getSession(UUID uuid) {
		EntityManager em = DatabaseConnection.getEntityManager();
		Session session = null;

		em.getTransaction().begin();
		try {
			TypedQuery<Session> q = em
					.createQuery(
							"SELECT Session FROM Session session WHERE session.sessID = :search",
							Session.class);
			q.setParameter("search", uuid).getSingleResult();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}

		return session;
	}
}
