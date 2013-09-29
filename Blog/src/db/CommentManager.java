package db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import data.Comment;
import data.Post;
import data.User;

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
	
	public List<Comment> getComments() {
		EntityManager em = DatabaseConnection.getEntityManager();

		List<Comment> allComments = new ArrayList<Comment>();

		em.getTransaction().begin();
		try {
			TypedQuery<Comment> q = em.createQuery("SELECT Comment FROM Comment comment",
					Comment.class);

			allComments = q.getResultList();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return allComments;
	}
}
