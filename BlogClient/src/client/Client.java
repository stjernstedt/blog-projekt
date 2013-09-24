package client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import managers.CommentManager;
import managers.PostManager;
import managers.UserManager;

import org.apache.axis2.AxisFault;

import core.CoreStub;
import core.CoreStub.Login;
import core.CoreStub.LoginResponse;
import core.CoreStub.Post;
import core.CoreStub.RemovePost;
import core.CoreStub.RemoveUser;
import core.CoreStub.User;

public class Client implements ActionListener {

	private Logger logg = Logger.getLogger("Client Logger");
	private CoreStub server = null;
	private String session;
	private PostManager pm = new PostManager();
	private UserManager um = new UserManager();
	private CommentManager cm = new CommentManager();

	private JFrame window = new JFrame("Blog Admin");
	private JFrame CPWindow;
	private JFrame CUWindow;
	private JFrame loginWindow;
	private JFrame EUWindow;
	private JFrame EPWindow;

	private Dimension knappDim = new Dimension(150, 25);

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private Border border = BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED);

	private LayoutManager lm = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();

	private JTextField usernameField = new JTextField(15);
	private JTextField passwordField = new JTextField(15);
	private JTextField emailField = new JTextField(15);
	private JTextField postTitleField = new JTextField(80);
	private JTextField loginUsername = new JTextField(15);

	private JPasswordField loginPassword = new JPasswordField(15);

	private JTextArea postTextArea = new JTextArea(20, 80);

	private String[] choices = { "Admin", "User", "Guest" };
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox usertypeCombobox = new JComboBox(choices);

	private JTable usersTable;
	private JTable postsTable;
	private DefaultTableModel usersModel = new DefaultTableModel();
	private DefaultTableModel postsModel = new DefaultTableModel();

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
		Container mainContent = window.getContentPane();

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		JButton manageUsers = new JButton("Manage Users");
		JButton managePosts = new JButton("Manage Posts");
		JButton loginButton = new JButton("Login");

		// Sätter storleken på knapparna
		loginButton.setPreferredSize(knappDim);
		manageUsers.setPreferredSize(knappDim);
		managePosts.setPreferredSize(knappDim);

		loginButton.addActionListener(this);
		loginButton.setActionCommand("openLogin");
		manageUsers.addActionListener(this);
		manageUsers.setActionCommand("openMU");
		managePosts.addActionListener(this);
		managePosts.setActionCommand("openMP");

		mainContent.setLayout(lm);

		resetConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		mainContent.add(loginButton, c);
		c.gridy = 1;
		mainContent.add(managePosts, c);
		c.gridy = 2;
		mainContent.add(manageUsers, c);

		window.pack();
		window.setLocationRelativeTo(null); // centrerar fönstret
		window.setVisible(true);
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

		try {
			result = server.login(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		return result.get_return();
	}

	// hantera användare fönstret
	private void manageUsersWindow() {
		JFrame MUWindow = new JFrame("Manage Users");
		Container MUcontent = MUWindow.getContentPane();
		JPanel pane = new JPanel();

		MUWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MUWindow.setResizable(false);
		MUWindow.setLayout(lm);

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

		MUWindow.pack();
		MUWindow.setLocationRelativeTo(null);
		MUWindow.setVisible(true);

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
		JLabel label4 = new JLabel("Usertype:");

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
		c.gridy = 3;
		CUcontent.add(label4, c);

		c.gridx = 1;
		c.gridy = 0;
		CUcontent.add(usernameField, c);
		c.gridy = 1;
		CUcontent.add(passwordField, c);
		c.gridy = 2;
		CUcontent.add(emailField, c);
		c.gridy = 3;
		usertypeCombobox.setSelectedIndex(1);
		CUcontent.add(usertypeCombobox, c);

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
	private void managePostsWindow() {
		JFrame MPWindow = new JFrame("Manage Post");
		Container MPcontent = MPWindow.getContentPane();
		JPanel pane = new JPanel();

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

		MPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MPWindow.setResizable(false);
		MPWindow.setLayout(lm);

		Post[] posts = pm.getPosts(server);
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

		MPWindow.pack();
		MPWindow.setLocationRelativeTo(null);
		MPWindow.setVisible(true);

	}

	// uppdaterar tabellen med användare
	private void updatePostsTable() {
		Post[] posts = pm.getPosts(server);

		postsModel.setRowCount(0);
		for (Post post : posts) {
			postsModel.addRow(new Object[] { post.getPostId(), post.getTitle(),
					post.getUserId(),
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
		Post[] posts = pm.getPosts(server);
		JFrame postsWindow = new JFrame("test");
		postsWindow.setLayout(lm);
		JPanel pane = new JPanel();
		pane.setLayout(lm);

		JScrollPane scroll = new JScrollPane(pane);
		pane.setAutoscrolls(true);
		scroll.setPreferredSize(new Dimension(500, 700));
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		postsWindow.add(scroll);

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		ArrayList<JTextArea> areas = new ArrayList<JTextArea>();

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
			c.gridy += 1;
			pane.add(fields.get(i), c);
			c.gridy += 2;
			pane.add(areas.get(i), c);
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

	// skapar login fönster
	private void loginWindow() {
		loginWindow = new JFrame("Login");
		Container loginContent = loginWindow.getContentPane();

		JButton loginButton = new JButton("Login");

		loginWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loginWindow.setResizable(false);
		loginWindow.setLayout(lm);

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
		loginButton.addActionListener(this);
		loginButton.setActionCommand("login");

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

		if ("openEU".equals(e.getActionCommand())) {
			editUserWindow(um.getUser(
					server,
					Integer.parseInt(usersTable.getModel()
							.getValueAt(usersTable.getSelectedRow(), 0)
							.toString())));
		}

		if ("openEP".equals(e.getActionCommand())) {
			editPostWindow(pm.getPost(
					server,
					Integer.parseInt(postsTable.getModel()
							.getValueAt(postsTable.getSelectedRow(), 0)
							.toString())));
		}

		if ("createUser".equals(e.getActionCommand())) {
			um.createUser(server, usernameField.getText(),
					passwordField.getText(), emailField.getText(),
					usertypeCombobox.getSelectedIndex());
			CUWindow.dispose();
			usernameField.setText("");
			passwordField.setText("");
			emailField.setText("");
			JOptionPane.showMessageDialog(null, "User created!");
			updateUsersTable();
		}

		if ("editUser".equals(e.getActionCommand())) {
			um.editUser(
					server,
					Integer.parseInt(usersModel.getValueAt(
							usersTable.getSelectedRow(), 0).toString()),
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
			arg.setUserId(Integer.parseInt(usersTable.getModel()
					.getValueAt(usersTable.getSelectedRow(), 0).toString()));

			try {
				server.removeUser(arg);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			updateUsersTable();
		}

		if ("createPost".equals(e.getActionCommand())) {
			pm.createPost(server, postTitleField.getText(),
					postTextArea.getText());
			CPWindow.dispose();
			postTitleField.setText("");
			postTextArea.setText("");
			JOptionPane.showMessageDialog(null, "Post created!");
			updatePostsTable();
		}

		if ("editPost".equals(e.getActionCommand())) {
			pm.editPost(
					server,
					Integer.parseInt(postsModel.getValueAt(
							postsTable.getSelectedRow(), 0).toString()),
					postTitleField.getText(), postTextArea.getText());
			EPWindow.dispose();
			postTitleField.setText("");
			postTextArea.setText("");
			JOptionPane.showMessageDialog(null, "Post updated!");
			updatePostsTable();

		}

		if ("removePost".equals(e.getActionCommand())) {
			RemovePost arg = new RemovePost();
			arg.setPostId(Integer.parseInt(postsModel.getValueAt(
					postsTable.getSelectedRow(), 0).toString()));

			try {
				server.removePost(arg);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			updatePostsTable();
		}

		if ("login".equals(e.getActionCommand())) {
			session = login(loginUsername.getText(),
					loginPassword.getPassword());
			JOptionPane.showMessageDialog(null, session);
		}

		if ("showPosts".equals(e.getActionCommand())) {
			showPosts();
		}

	}
}
