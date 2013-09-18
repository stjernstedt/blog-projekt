package managers;

import java.rmi.RemoteException;
import java.util.Date;

import core.CoreStub;
import core.CoreStub.CreateComment;

public class CommentManager {

	public void createComment(CoreStub server, String email, String text, String name,
			Date date, int userID) {
		CreateComment comment = new CreateComment();
		comment.setEmail(email);
		comment.setName(name);
		comment.setText(text);
		comment.setDate(date);
		comment.setUserID(userID);

		try {
			server.createComment(comment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
