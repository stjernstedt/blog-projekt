package db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import data.Post;

public class PostManager {

	private static DatabaseConnection connection;
	private static PostManager postManager = new PostManager();

	private PostManager() {
		connection = DatabaseConnection.getInstance();
	}

	public static PostManager getInstance() {
		return postManager;
	}

	
	//sparar ett inlägg i databasen
	public Post createPost(Post post) {
		EntityManager em = connection.getEntityManager();
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
	
	// tar bort ett inlägg
	public void removePost(int postId) {
		EntityManager em = connection.getEntityManager();
		
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
	
	public List<Post> getPosts() {
		EntityManager em = connection.getEntityManager();
		List<Post> allPosts = new ArrayList<Post>();
		
		em.getTransaction().begin();
		try {
			TypedQuery<Post> q = em.createQuery("SELECT Post FROM Post post", Post.class);
			allPosts = q.getResultList();
			em.getTransaction().commit();
		} finally {
			if(em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
		return allPosts;
	}
}
