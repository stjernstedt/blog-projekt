package managers;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import core.CoreStub;
import core.CoreStub.CreatePost;
import core.CoreStub.EditPost;
import core.CoreStub.GetPost;
import core.CoreStub.GetPostResponse;
import core.CoreStub.GetPosts;
import core.CoreStub.GetPostsResponse;
import core.CoreStub.Post;
import core.CoreStub.RemovePost;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
public class PostManager {

	private static Logger logg = Logger.getLogger("postManager");

	public void createPost(CoreStub server, String title, String text,
			String username) {
		CreatePost post = new CreatePost();
		post.setText(text);
		post.setTitle(title);
		post.setUsername(username);

		try {
			server.createPost(post);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Post[] getPosts(CoreStub server, String session) {
		logg.info("postmanager session: " + session);
		GetPosts arg = new GetPosts();
		GetPostsResponse result = null;
		
		arg.setSessionKey(session);

		try {
			result = server.getPosts(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}

		return result.get_return();
	}

	public Post getPost(CoreStub server, int postId) {
		GetPost arg = new GetPost();
		GetPostResponse result = null;
		arg.setPostId(postId);

		try {
			result = server.getPost(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}

		return result.get_return();
	}

	public void editPost(CoreStub server, int postId, String title, String text) {
		EditPost post = new EditPost();
		post.setPostId(postId);
		post.setTitle(title);
		post.setText(text);

		try {
			server.editPost(post);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void removePost(CoreStub server, int postId) {
		RemovePost arg = new RemovePost();
		arg.setPostId(postId);

		try {
			server.removePost(arg);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
}
