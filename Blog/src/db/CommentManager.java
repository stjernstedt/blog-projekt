package db;

import javax.persistence.EntityManager;

import data.Comment;

public class CommentManager {

	private static CommentManager commentManager = new CommentManager();

	private CommentManager() {
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

	public void removeComment(int commentID) {
		EntityManager em = DatabaseConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			Comment comment = em.find(Comment.class, commentID);
			em.remove(comment);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}
}
