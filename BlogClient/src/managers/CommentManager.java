package managers;

import java.rmi.RemoteException;

import core.CoreStub;
import core.CoreStub.Comment;
import core.CoreStub.CreateComment;
import core.CoreStub.GetComments;
import core.CoreStub.GetCommentsResponse;
import core.CoreStub.RemoveComment;
import core.CoreStub.RemovePost;

public class CommentManager {

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
}
