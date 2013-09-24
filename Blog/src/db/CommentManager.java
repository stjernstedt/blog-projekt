package db;

import javax.persistence.EntityManager;

import data.Comment;

public class CommentManager {

//	private static DatabaseConnection connection;
	private static CommentManager commentManager = new CommentManager();

	private CommentManager() {
//		connection = DatabaseConnection.getInstance();
	}

	public static CommentManager getInstance() {
		return commentManager;
	}

	public Comment createComment(Comment comment) {
		EntityManager em = DatabaseConnection.getEntityManager();
		em.getTransaction().begin();

		try {
			em.persist(comment);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}

		return comment;
	}
}
