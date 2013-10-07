package managers;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import core.CoreStub;
import core.CoreStub.Comment;
import core.CoreStub.CreateComment;
import core.CoreStub.GetComments;
import core.CoreStub.GetCommentsResponse;
import core.CoreStub.GetPostComments;
import core.CoreStub.GetPostCommentsResponse;
import core.CoreStub.RemoveComment;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
public class CommentManager {
	
	private static Logger logg = Logger.getLogger("commentManager");

	public void createComment(CoreStub server, String email, String text, String name, int postId) {
		CreateComment comment = new CreateComment();
		comment.setEmail(email);
		comment.setName(name);
		comment.setText(text);
		comment.setPostId(postId);

		try {
			server.createComment(comment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Comment[] getComments(CoreStub server) {
		GetComments comments = new GetComments();
		GetCommentsResponse result = new GetCommentsResponse();
		
		try {
			result = server.getComments(comments);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return result.get_return();
	}
	
	public void removeComment(CoreStub server, int commentId) {
		RemoveComment arg = new RemoveComment();
		arg.setCommentID(commentId);

		try {
			server.removeComment(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Comment[] getPostComments(CoreStub server, int postId) {
		GetPostComments comments = new GetPostComments();
		GetPostCommentsResponse result = new GetPostCommentsResponse();
		
		comments.setPostId(postId);
		
		try {
			result = server.getPostComments(comments);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		if (result.get_return().length == 0) {
			Comment[] temp = null;
			return temp;
		}
		logg.info("från server: "+result.get_return());
		return result.get_return();
	}
}
