package managers;

import java.rmi.RemoteException;

import core.CoreStub;
import core.CoreStub.CreatePost;
import core.CoreStub.GetPosts;
import core.CoreStub.GetPostsResponse;
import core.CoreStub.Post;

public class PostManager {

	public void createPost(CoreStub server, String title, String text) {
		CreatePost post = new CreatePost();
		// post.setDate(date);
		post.setText(text);
		post.setTitle(title);
		// post.setUserId(userId);

		try {
			server.createPost(post);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Post[] getPosts(CoreStub server) {
		GetPosts arg = new GetPosts();
		GetPostsResponse result = null;
		
		try {
			result = server.getPosts(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		
		return result.get_return();
	}
}
