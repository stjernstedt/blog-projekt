package db;

import javax.persistence.EntityManager;

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
	
	public Post createPost(Post post) {
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		try {
			em.persist(post);
			em.getTransaction().commit();
		} finally {
			if(em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
		em.close();
		
		return post;
	}
}
