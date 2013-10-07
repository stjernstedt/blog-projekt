package db;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import data.Post;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
public class PostManager {

	private static PostManager postManager = new PostManager();
	private static Logger logg = Logger.getLogger("postManager");

	private PostManager() {
	}

	public static PostManager getInstance() {
		return postManager;
	}

	// sparar ett inlägg i databasen
	public Post createPost(Post post) {
		EntityManager em = DatabaseConnection.getEntityManager();
		em.getTransaction().begin();

		try {
			em.persist(post);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}

		return post;
	}

	// editera inlägg
	public void editPost(int postId, String title, String text) {
		EntityManager em = DatabaseConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			Post post = em.find(Post.class, postId);
//			logg.info("editerar post: "+post+" med data: "+title+" "+text);
			post.setTitle(title);
			post.setText(text);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

	// tar bort ett inlägg
	public void removePost(int postId) {
		EntityManager em = DatabaseConnection.getEntityManager();

		em.getTransaction().begin();
		try {
			Post post = em.find(Post.class, postId);
			em.remove(post);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

	// hämtar alla inlägg
	public List<Post> getPosts(String username, int userType) {
		EntityManager em = DatabaseConnection.getEntityManager();

		List<Post> allPosts = new ArrayList<Post>();

		em.getTransaction().begin();
		try {
			TypedQuery<Post> q;
			if (userType == 0) {
				q = em.createQuery("SELECT Post FROM Post post", Post.class);
			} else {
				q = em.createQuery(
						"SELECT Post FROM Post post WHERE post.username = :username",
						Post.class);
				q.setParameter("username", username);
			}
			allPosts = q.getResultList();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return allPosts;
	}

	// hämtar specifikt inlägg
	public Post getPost(int postId) {
		EntityManager em = DatabaseConnection.getEntityManager();
		Post post = null;

		em.getTransaction().begin();
		try {
			post = em.find(Post.class, postId);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return post;
	}
}
