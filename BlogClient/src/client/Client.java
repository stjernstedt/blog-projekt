package client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import managers.CommentManager;
import managers.PostManager;
import managers.SessionManager;
import managers.UserManager;

import org.apache.axis2.AxisFault;

import core.CoreStub;
import core.CoreStub.Comment;
import core.CoreStub.Login;
import core.CoreStub.LoginResponse;
import core.CoreStub.Post;
import core.CoreStub.RemoveUser;
import core.CoreStub.User;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
public class Client implements ActionListener {

	private Logger logg = Logger.getLogger("Client Logger");
	private CoreStub server = null;
	private String session = null;
	private PostManager pm = new PostManager();
	private UserManager um = new UserManager();
	private CommentManager cm = new CommentManager();
	private SessionManager sm = new SessionManager();

	private JFrame window = new JFrame("Blog Admin");
	private JFrame CPWindow;
	private JFrame CUWindow;
	private JFrame loginWindow;
	private JFrame EUWindow;
	private JFrame EPWindow;
	private JFrame CCWindow;

	private Dimension knappDim = new Dimension(150, 25);

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private Border border = BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED);

	private LayoutManager lm = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();

	private List<JButton> commentButtons;
	private List<JButton> viewCommentsButtons;

	private JTextField usernameField = new JTextField(15);
	private JTextField passwordField = new JTextField(15);
	private JTextField emailField = new JTextField(15);
	private JTextField postTitleField = new JTextField(80);
	private JTextField loginUsername = new JTextField(15);

	private JPasswordField loginPassword = new JPasswordField(15);

	private JTextArea postTextArea = new JTextArea(20, 80);
	private JTextArea commentTextArea = new JTextArea(10, 30);

	private String[] choices = { "Admin", "User", "Guest" };
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox usertypeCombobox = new JComboBox(choices);

	private JTable usersTable;
	private JTable postsTable;
	private JTable commentsTable;
	private DefaultTableModel usersModel = new DefaultTableModel();
	private DefaultTableModel postsModel = new DefaultTableModel();
	private DefaultTableModel commentsModel = new DefaultTableModel();

	public static void main(String[] args) {

		Client client = new Client();
		client.start();
		// System.exit(0);
	}

	private Client() {
		server = initializeServer();
	}

	/**
	 * Metoden start() Är det nya main(String[] args)
	 */
	public void start() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);

		loginWindow();
	}

	private static CoreStub initializeServer() {
		CoreStub server = null;
		try {
			server = new CoreStub();
		} catch (AxisFault e) {
			System.err.println("Fel vid skapandet av server-stubben");
			e.printStackTrace();
			System.exit(-1);
		}
		return server;
	}

	// login metod
	private String login(String username, char[] password) {
		Login arg = new Login();
		LoginResponse result = null;
		String pwd = "";

		for (char c : password) {
			pwd += c;
		}

		arg.setUsername(username);
		arg.setPassword(pwd);

		logg.info("försöker skicka info");
		try {
			result = server.login(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		logg.info("får svar: " + result.get_return());
		return result.get_return();
	}

	private void adminView() {
		Container content = window.getContentPane();
		JPanel mainContent = new JPanel();
		JTabbedPane tabs = new JTabbedPane();

		mainContent.setLayout(lm);

		tabs.addTab("Manage Users", manageUsersWindow());
		tabs.addTab("Manage Posts", managePostsWindow());
		tabs.addTab("Manage Comments", manageCommentsWindow());

		mainContent.add(tabs);
		content.add(mainContent);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	private void userView() {
		Container content = window.getContentPane();
		JPanel mainContent = new JPanel();
		JTabbedPane tabs = new JTabbedPane();

		mainContent.setLayout(lm);

		tabs.addTab("Manage Posts", managePostsWindow());
		tabs.addTab("Manage Comments", manageCommentsWindow());

		mainContent.add(tabs);
		content.add(mainContent);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	// hantera användare fönstret
	private JPanel manageUsersWindow() {
		JPanel MUcontent = new JPanel();
		JPanel pane = new JPanel();

		MUcontent.setLayout(lm);

		JButton createUser = new JButton("Create User");
		JButton removeUser = new JButton("Remove User");
		JButton editUser = new JButton("Edit User");

		createUser.setPreferredSize(knappDim);
		editUser.setPreferredSize(knappDim);
		removeUser.setPreferredSize(knappDim);

		createUser.addActionListener(this);
		editUser.addActionListener(this);
		removeUser.addActionListener(this);

		User[] users = um.getUsers(server);
		String[] columnNames = { "UserID", "Username", "Password", "Email",
				"User Type" };

		usersTable = new JTable(users.length, columnNames.length);
		usersTable.setModel(usersModel);
		usersTable.setSelectionMode(0);

		usersModel.setColumnCount(0);
		for (int i = 0; i < 5; i++) {
			usersModel.addColumn(columnNames[i]);
		}
		updateUsersTable();

		JScrollPane scrollPane = new JScrollPane(usersTable);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setAutoscrolls(true);

		resetConstraints();
		pane.add(scrollPane);
		c.insets = new Insets(5, 5, 5, 5);
		MUcontent.add(createUser, c);
		c.gridy = 1;
		MUcontent.add(editUser, c);
		c.gridy = 2;
		MUcontent.add(removeUser, c);
		c.gridy = 0;
		c.gridx = 1;
		c.gridheight = 5;
		MUcontent.add(pane, c);

		editUser.setActionCommand("openEU");
		removeUser.setActionCommand("removeUser");
		createUser.setActionCommand("openCU");

		return MUcontent;
	}

	// uppdaterar tabellen med användare
	private void updateUsersTable() {
		User[] users = um.getUsers(server);

		usersModel.setRowCount(0);
		for (User user : users) {
			usersModel.addRow(new Object[] { user.getUserId(),
					user.getUsername(), user.getPassword(), user.getEmail(),
					user.getUsertype() });
		}
	}

	// skapar fönstret för att skapa användare
	private void createUserWindow() {
		CUWindow = new JFrame("Create User");
		Container CUcontent = CUWindow.getContentPane();

		JLabel label1 = new JLabel("Username:");
		JLabel label2 = new JLabel("Password:");
		JLabel label3 = new JLabel("Email:");
		// JLabel label4 = new JLabel("Usertype:");

		JButton CUbutton = new JButton("Create User");
		CUbutton.addActionListener(this);

		CUWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CUWindow.setResizable(false);
		CUWindow.setLayout(lm);

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		CUcontent.add(label1, c);
		c.gridy = 1;
		CUcontent.add(label2, c);
		c.gridy = 2;
		CUcontent.add(label3, c);
		// c.gridy = 3;
		// CUcontent.add(label4, c);

		c.gridx = 1;
		c.gridy = 0;
		CUcontent.add(usernameField, c);
		c.gridy = 1;
		CUcontent.add(passwordField, c);
		c.gridy = 2;
		CUcontent.add(emailField, c);
		// c.gridy = 3;
		// usertypeCombobox.setSelectedIndex(1);
		// CUcontent.add(usertypeCombobox, c);

		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 0.1;
		c.weighty = 1;

		CUbutton.setActionCommand("createUser");
		CUcontent.add(CUbutton, c);

		CUWindow.pack();
		CUWindow.setLocationRelativeTo(null);
		CUWindow.setVisible(true);
	}

	// skapar edit user fönstret
	private void editUserWindow(User user) {
		EUWindow = new JFrame("Edit User");
		Container EUcontent = EUWindow.getContentPane();

		JLabel label1 = new JLabel("Username:");
		JLabel label2 = new JLabel("Password:");
		JLabel label3 = new JLabel("Email:");
		JLabel label4 = new JLabel("Usertype:");

		JButton EUbutton = new JButton("Apply Changes");
		EUbutton.addActionListener(this);

		EUWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		EUWindow.setResizable(false);
		EUWindow.setLayout(lm);

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		EUcontent.add(label1, c);
		c.gridy = 1;
		EUcontent.add(label2, c);
		c.gridy = 2;
		EUcontent.add(label3, c);
		c.gridy = 3;
		EUcontent.add(label4, c);

		c.gridx = 1;
		c.gridy = 0;
		EUcontent.add(usernameField, c);
		c.gridy = 1;
		EUcontent.add(passwordField, c);
		c.gridy = 2;
		EUcontent.add(emailField, c);
		c.gridy = 3;
		usertypeCombobox.setSelectedIndex(user.getUsertype());
		EUcontent.add(usertypeCombobox, c);
		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 0.1;
		c.weighty = 1;
		EUcontent.add(EUbutton, c);

		EUbutton.setActionCommand("editUser");

		usernameField.setText(user.getUsername());
		passwordField.setText(user.getPassword());
		emailField.setText(user.getEmail());

		EUWindow.pack();
		EUWindow.setLocationRelativeTo(null);
		EUWindow.setVisible(true);
	}

	// hantera inlägg fönstret
	private JPanel managePostsWindow() {
		JPanel MPcontent = new JPanel();
		JPanel pane = new JPanel();

		MPcontent.setLayout(lm);

		JButton updatePost = new JButton("Edit Post");
		JButton showPosts = new JButton("Show Posts");
		JButton removePost = new JButton("Remove Post");
		JButton createPost = new JButton("Create Post");

		updatePost.setPreferredSize(knappDim);
		removePost.setPreferredSize(knappDim);
		createPost.setPreferredSize(knappDim);
		showPosts.setPreferredSize(knappDim);

		createPost.addActionListener(this);
		removePost.addActionListener(this);
		showPosts.addActionListener(this);
		updatePost.addActionListener(this);

		createPost.setActionCommand("openCP");
		removePost.setActionCommand("removePost");
		showPosts.setActionCommand("showPosts");
		updatePost.setActionCommand("openEP");

		logg.info("managePosts session: " + session);
		Post[] posts = pm.getPosts(server, session);
		String[] columnNames = { "PostID", "Title", "Author", "Date" };

		postsTable = new JTable(posts.length, columnNames.length);
		postsTable.setModel(postsModel);
		postsTable.setSelectionMode(0);

		postsModel.setColumnCount(0);
		for (int i = 0; i < columnNames.length; i++) {
			postsModel.addColumn(columnNames[i]);
		}
		postsTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		postsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		postsTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		postsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
		updatePostsTable();

		JScrollPane scrollPane = new JScrollPane(postsTable);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setAutoscrolls(true);

		pane.add(scrollPane);

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		MPcontent.add(createPost, c);
		c.gridy = 1;
		MPcontent.add(removePost, c);
		c.gridy = 2;
		MPcontent.add(updatePost, c);
		c.gridy = 3;
		MPcontent.add(showPosts, c);
		c.gridy = 0;
		c.gridx = 1;
		c.gridheight = 5;
		MPcontent.add(pane, c);

		return MPcontent;
	}

	// uppdaterar tabellen med användare
	private void updatePostsTable() {
		logg.info("updatePosts session: " + session);
		Post[] posts = pm.getPosts(server, session);

		postsModel.setRowCount(0);
		for (Post post : posts) {
			postsModel.addRow(new Object[] { post.getPostId(), post.getTitle(),
					post.getUsername(),
					dateFormatter.format(post.getDate().getTime()) });
		}
	}

	// skapar fönstret för att göra inlägg
	private void createPostWindow() {
		CPWindow = new JFrame("Create Post");
		Container CPcontent = CPWindow.getContentPane();

		JButton CPbutton = new JButton("Post");
		CPbutton.addActionListener(this);

		CPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CPWindow.setResizable(false);
		CPWindow.setLayout(lm);

		postTextArea.setBorder(border);
		postTextArea.setLineWrap(true);

		JLabel CPlabel1 = new JLabel("Title");
		JLabel CPlabel2 = new JLabel("Text");

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		CPcontent.add(CPlabel1, c);
		c.gridy = 1;
		CPcontent.add(CPlabel2, c);

		c.gridx = 1;
		c.gridy = 0;
		CPcontent.add(postTitleField, c);
		c.gridy = 1;
		CPcontent.add(postTextArea, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.1;
		c.gridy = 2;
		CPcontent.add(CPbutton, c);

		CPbutton.setActionCommand("createPost");
		CPcontent.add(CPbutton, c);

		CPWindow.pack();
		CPWindow.setLocationRelativeTo(null);
		CPWindow.setVisible(true);
	}

	// skapar ett fönster och visar alla inlägg
	private void showPosts() {
		JFrame postsWindow = new JFrame("test");

		postsWindow.setResizable(false);
		postsWindow.setLayout(lm);
		JPanel pane = new JPanel();
		pane.setLayout(lm);

		Post[] posts = pm.getPosts(server, session);

		JScrollPane scroll = new JScrollPane(pane);
		pane.setAutoscrolls(true);
		scroll.setPreferredSize(new Dimension(500, 700));
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		postsWindow.add(scroll);

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		ArrayList<JTextArea> areas = new ArrayList<JTextArea>();
		commentButtons = new ArrayList<JButton>();
		viewCommentsButtons = new ArrayList<JButton>();
		List<JPanel> buttonsPanels = new ArrayList<JPanel>();

		int i = 0;
		resetConstraints();
		c.gridx = 0;
		c.gridy = 0;
		for (Post post : posts) {
			fields.add(new JTextField(post.getTitle(), 43));
			fields.get(i).setEditable(false);

			areas.add(new JTextArea(post.getText(), 20, 43));
			areas.get(i).setEditable(false);
			areas.get(i).setLineWrap(true);

			commentButtons.add(new JButton("Comment"));
			commentButtons.get(i).putClientProperty("postId", post.getPostId());
			commentButtons.get(i).addActionListener(this);
			commentButtons.get(i).setActionCommand("openCC");

			viewCommentsButtons.add(new JButton("View Comments"));
			viewCommentsButtons.get(i).putClientProperty("postId",
					post.getPostId());
			viewCommentsButtons.get(i).addActionListener(this);
			viewCommentsButtons.get(i).setActionCommand("openVC");

			buttonsPanels.add(new JPanel());
			buttonsPanels.get(i).setLayout(new FlowLayout());
			buttonsPanels.get(i).add(viewCommentsButtons.get(i), c);
			buttonsPanels.get(i).add(commentButtons.get(i), c);

			c.gridy += 1;
			fields.get(i).setBorder(border);
			pane.add(fields.get(i), c);
			c.gridy += 2;
			areas.get(i).setBorder(border);
			pane.add(areas.get(i), c);
			c.gridy += 3;
			c.anchor = GridBagConstraints.LINE_END;
			c.insets = new Insets(0, 0, 5, 0);
			pane.add(buttonsPanels.get(i), c);
			c.anchor = 10;

			i++;
		}

		postsWindow.pack();
		postsWindow.setLocationRelativeTo(null);
		postsWindow.setVisible(true);
	}

	// skapar fönstret för att ändra på inlägg.
	private void editPostWindow(Post post) {
		EPWindow = new JFrame("Edit Post");
		Container EPcontent = EPWindow.getContentPane();

		JButton EPbutton = new JButton("Apply changes");
		EPbutton.addActionListener(this);
		EPbutton.setActionCommand("editPost");

		EPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		EPWindow.setResizable(false);
		EPWindow.setLayout(lm);

		postTextArea.setBorder(border);
		postTextArea.setLineWrap(true);

		JLabel EPlabel1 = new JLabel("Title");
		JLabel EPlabel2 = new JLabel("Text");
		postTitleField.setText(post.getTitle());
		postTextArea.setText(post.getText());

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		EPcontent.add(EPlabel1, c);
		c.gridy = 1;
		EPcontent.add(EPlabel2, c);

		c.gridx = 1;
		c.gridy = 0;

		EPcontent.add(postTitleField, c);
		c.gridy = 1;
		EPcontent.add(postTextArea, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.1;
		c.gridy = 2;
		EPcontent.add(EPbutton, c);

		EPWindow.pack();
		EPWindow.setLocationRelativeTo(null);
		EPWindow.setVisible(true);

	}

	// skapar fönstret för att hantera kommentarer
	private JPanel manageCommentsWindow() {
		JPanel MCcontent = new JPanel();
		JPanel pane = new JPanel();

		MCcontent.setLayout(lm);

		JButton updateComment = new JButton("Edit Comment");
		JButton removeComment = new JButton("Remove Comment");

		updateComment.setPreferredSize(knappDim);
		removeComment.setPreferredSize(knappDim);

		removeComment.addActionListener(this);
		updateComment.addActionListener(this);

		removeComment.setActionCommand("removeComment");
		updateComment.setActionCommand("openEC");

		Comment[] comments = cm.getComments(server);
		String[] columnNames = { "CommentId", "Name", "Email", "Comment",
				"PostId" };

		commentsTable = new JTable(comments.length, columnNames.length);
		commentsTable.setModel(commentsModel);
		commentsTable.setSelectionMode(0);

		commentsModel.setColumnCount(0);
		for (int i = 0; i < columnNames.length; i++) {
			commentsModel.addColumn(columnNames[i]);
		}
		commentsTable.getColumnModel().getColumn(0).setPreferredWidth(1);
		commentsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		commentsTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		commentsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
		updateCommentsTable();

		JScrollPane scrollPane = new JScrollPane(commentsTable);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setAutoscrolls(true);

		pane.add(scrollPane);

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		MCcontent.add(removeComment, c);
		c.gridy = 1;
		MCcontent.add(updateComment, c);
		c.gridy = 0;
		c.gridx = 1;
		c.gridheight = 5;
		MCcontent.add(pane, c);

		return MCcontent;
	}

	// skapar fönstret för att göra kommentarer
	private void createCommentWindow(int postId) {
		CCWindow = new JFrame("Create Comment");
		Container CCcontent = CCWindow.getContentPane();

		JButton CCbutton = new JButton("Post Comment");
		CCbutton.putClientProperty("postId", postId);
		CCbutton.addActionListener(this);

		CCWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CCWindow.setResizable(false);
		CCWindow.setLayout(lm);

		commentTextArea.setBorder(border);
		commentTextArea.setLineWrap(true);

		JLabel CClabel = new JLabel("Name");
		JLabel CClabel2 = new JLabel("Email");
		JLabel CClabel3 = new JLabel("Text");

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		CCcontent.add(CClabel, c);
		c.gridy = 1;
		CCcontent.add(CClabel2, c);
		c.gridy = 2;
		CCcontent.add(CClabel3, c);

		c.gridy = 0;
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		CCcontent.add(usernameField, c);
		c.gridy = 1;
		CCcontent.add(emailField, c);
		c.gridx = 1;
		c.gridy = 2;
		CCcontent.add(commentTextArea, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.1;
		c.gridy = 3;
		CCcontent.add(CCbutton, c);

		CCbutton.setActionCommand("createComment");
		CCcontent.add(CCbutton, c);

		CCWindow.pack();
		CCWindow.setLocationRelativeTo(null);
		CCWindow.setVisible(true);

	}

	private void updateCommentsTable() {
		Comment[] comments = cm.getComments(server);

		commentsModel.setRowCount(0);
		for (Comment comment : comments) {
			commentsModel.addRow(new Object[] { comment.getCommentId(),
					comment.getName(), comment.getEmail(), comment.getText(),
					comment.getPostId() });
		}
	}

	private void showComments(int postId) {
		JFrame commentsWindow = new JFrame("Comments");

		commentsWindow.setResizable(false);
		commentsWindow.setLayout(lm);
		JPanel pane = new JPanel();
		pane.setLayout(lm);
		Comment[] comments = null;

		try {
			comments = cm.getPostComments(server, postId);
		} catch (Exception e) {
				commentsWindow.dispose();
				JOptionPane.showMessageDialog(null, "No comments yet!");
		}
		JScrollPane scroll = new JScrollPane(pane);
		pane.setAutoscrolls(true);
		scroll.setPreferredSize(new Dimension(500, 700));
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		commentsWindow.add(scroll);

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		ArrayList<JTextArea> areas = new ArrayList<JTextArea>();

		int i = 0;
		resetConstraints();
		c.gridx = 0;
		c.gridy = 0;
		for (Comment comment : comments) {
			fields.add(new JTextField(comment.getName(), 43));
			fields.get(i).setEditable(false);

			areas.add(new JTextArea(comment.getText(), 20, 43));
			areas.get(i).setEditable(false);
			areas.get(i).setLineWrap(true);

			c.gridy += 1;
			fields.get(i).setBorder(border);
			pane.add(fields.get(i), c);
			c.gridy += 2;
			c.insets = new Insets(0, 0, 5, 0);
			areas.get(i).setBorder(border);
			pane.add(areas.get(i), c);

			i++;
		}

		commentsWindow.pack();
		commentsWindow.setLocationRelativeTo(null);
		commentsWindow.setVisible(true);
	}

	// skapar login fönster
	private void loginWindow() {
		loginWindow = new JFrame("Login");
		Container loginContent = loginWindow.getContentPane();

		loginWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loginWindow.setResizable(false);
		loginWindow.setLayout(lm);

		JButton loginButton = new JButton("Login");
		JButton newUser = new JButton("Create User");

		JLabel loginLabel1 = new JLabel("Username:");
		JLabel loginlabel2 = new JLabel("Password:");

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		loginContent.add(loginLabel1, c);
		c.gridy = 1;
		loginContent.add(loginlabel2, c);
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 0;
		loginContent.add(loginUsername, c);
		c.gridy = 1;
		loginContent.add(loginPassword, c);
		c.anchor = GridBagConstraints.LINE_END;
		c.gridy = 2;
		loginContent.add(loginButton, c);
		c.anchor = GridBagConstraints.LINE_START;
		loginContent.add(newUser, c);

		loginButton.addActionListener(this);
		loginButton.setActionCommand("login");
		newUser.addActionListener(this);
		newUser.setActionCommand("openCU");

		loginWindow.pack();
		loginWindow.setLocationRelativeTo(null);
		loginWindow.setVisible(true);
	}

	// återställer alla GridBagConstraints
	private void resetConstraints() {
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = 10;
		c.gridheight = 1;
		c.gridwidth = 1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("openCU".equals(e.getActionCommand()))
			createUserWindow();

		if ("openCP".equals(e.getActionCommand()))
			createPostWindow();

		if ("openLogin".equals(e.getActionCommand()))
			loginWindow();

		if ("openMU".equals(e.getActionCommand()))
			manageUsersWindow();

		if ("openMP".equals(e.getActionCommand()))
			managePostsWindow();

		if ("openCC".equals(e.getActionCommand())) {
			JButton temp = (JButton) e.getSource();
			int postId = (int) temp.getClientProperty("postId");
			createCommentWindow(postId);
		}

		if ("openVC".equals(e.getActionCommand())) {
			JButton temp = (JButton) e.getSource();
			showComments((int) temp.getClientProperty("postId"));
		}

		if ("openEU".equals(e.getActionCommand())) {
			editUserWindow(um.getUser(server, (int) usersTable.getModel()
					.getValueAt(usersTable.getSelectedRow(), 0)));
		}

		if ("openEP".equals(e.getActionCommand())) {
			editPostWindow(pm.getPost(server, (int) postsTable.getModel()
					.getValueAt(postsTable.getSelectedRow(), 0)));
		}

		if ("createUser".equals(e.getActionCommand())) {
			if (um.searchUser(server, usernameField.getText()) != null) {
				JOptionPane.showMessageDialog(null, "Username already exists!");
			} else {
				um.createUser(server, usernameField.getText(),
						passwordField.getText(), emailField.getText(), 1);
				CUWindow.dispose();
				usernameField.setText("");
				passwordField.setText("");
				emailField.setText("");
				JOptionPane.showMessageDialog(null, "User created!");
				updateUsersTable();
			}
		}

		if ("editUser".equals(e.getActionCommand())) {
			um.editUser(
					server,
					(int) usersModel.getValueAt(usersTable.getSelectedRow(), 0),
					usernameField.getText(), passwordField.getText(),
					emailField.getText(), usertypeCombobox.getSelectedIndex());
			EUWindow.dispose();
			usernameField.setText("");
			passwordField.setText("");
			emailField.setText("");
			JOptionPane.showMessageDialog(null, "User updated!");
			updateUsersTable();
		}

		if ("removeUser".equals(e.getActionCommand())) {
			RemoveUser arg = new RemoveUser();
			arg.setUserId((int) usersTable.getModel().getValueAt(
					usersTable.getSelectedRow(), 0));

			try {
				server.removeUser(arg);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			updateUsersTable();
		}

		if ("createPost".equals(e.getActionCommand())) {
			pm.createPost(server, postTitleField.getText(), postTextArea
					.getText(), sm.getSession(server, session).getUsername());
			CPWindow.dispose();
			postTitleField.setText("");
			postTextArea.setText("");
			JOptionPane.showMessageDialog(null, "Post created!");
			updatePostsTable();
		}

		if ("editPost".equals(e.getActionCommand())) {
			pm.editPost(
					server,
					(int) postsModel.getValueAt(postsTable.getSelectedRow(), 0),
					postTitleField.getText(), postTextArea.getText());
			EPWindow.dispose();
			postTitleField.setText("");
			postTextArea.setText("");
			JOptionPane.showMessageDialog(null, "Post updated!");
			updatePostsTable();

		}

		if ("removePost".equals(e.getActionCommand())) {
			pm.removePost(server,
					(int) postsModel.getValueAt(postsTable.getSelectedRow(), 0));
			updatePostsTable();
		}

		if ("createComment".equals(e.getActionCommand())) {
			JButton temp = (JButton) e.getSource();
			cm.createComment(server, emailField.getText(),
					commentTextArea.getText(), usernameField.getText(),
					(int) temp.getClientProperty("postId"));
			CCWindow.dispose();
			emailField.setText("");
			commentTextArea.setText("");
			usernameField.setText("");
			JOptionPane.showMessageDialog(null, "Comment Created");
			updateCommentsTable();
		}

		if ("removeComment".equals(e.getActionCommand())) {
			cm.removeComment(
					server,
					(int) commentsModel.getValueAt(
							commentsTable.getSelectedRow(), 0));
			updateCommentsTable();
		}

		if ("login".equals(e.getActionCommand())) {
			session = login(loginUsername.getText(),
					loginPassword.getPassword());
			logg.info("får en sessionkey: " + session);
			if (!session.equals("error")) {
				if (sm.getSession(server, session).getUserType() == 0) {
					adminView();
					loginWindow.dispose();
				} else {
					userView();
					loginWindow.dispose();
					window.setVisible(true);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Wrong username or password!");
			}
		}

		if ("showPosts".equals(e.getActionCommand())) {
			showPosts();
		}

	}
}