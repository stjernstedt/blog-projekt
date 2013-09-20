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
	private JFrame MUWindow;
	private JFrame MPWindow;
	private JFrame EUWindow;

	private Border border = BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED);

	private Container mainContent = window.getContentPane();
	private LayoutManager lm = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();

	private JTextField textField1 = new JTextField(15);
	private JTextField textField2 = new JTextField(15);
	private JTextField textField3 = new JTextField(15);
	private JTextField CPtitle = new JTextField(80);
	private JTextField loginUsername = new JTextField(15);
	private JTextField delUserField = new JTextField(15);

	private JPasswordField loginPassword = new JPasswordField(15);

	private JTextArea usersBox = new JTextArea(10, 40);
	private JTextArea CPpostBox = new JTextArea(20, 80);

	private String[] choices = { "Admin", "User", "Guest" };
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox combobox = new JComboBox(choices);

	JTable usersTable;
	DefaultTableModel model = new DefaultTableModel();

	private JButton manageUser = new JButton("Manage Users");
	private JButton managePost = new JButton("Manage Posts");
	private JButton button1 = new JButton("Create User");
	private JButton button2 = new JButton("Create Post");
	private JButton removePost = new JButton("Remove Post");
	private JButton updatePost = new JButton("Edit Posts");
	private JButton button3 = new JButton("Login");
	private JButton CUbutton = new JButton("Create User");
	private JButton EUbutton = new JButton("Apply Changes");
	private JButton deleteUser = new JButton("Remove User");
	private JButton editUser = new JButton("Edit User");
	private JButton GUbutton = new JButton("Get Users");
	private JButton CPbutton = new JButton("Post");
	private JButton loginButton = new JButton("Login");
	private JButton showPosts = new JButton("Show Posts");

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
		resetConstraints();

		c.insets = new Insets(5, 5, 5, 5);

		usersBox.setBorder(border);
		mainContent.setLayout(lm);

		mainContent.add(button3, c);
		c.gridy = 1;
		mainContent.add(managePost, c);
		c.gridx = 0;
		c.gridy = 2;
		mainContent.add(manageUser, c);
		c.gridy = 3;
		mainContent.add(delUserField);
		c.gridy = 4;

		// Sätter storleken på knapparna
		Dimension knappDim = new Dimension(150, 25);
		button1.setPreferredSize(knappDim);
		button2.setPreferredSize(knappDim);
		button3.setPreferredSize(knappDim);
		GUbutton.setPreferredSize(knappDim);
		manageUser.setPreferredSize(knappDim);
		managePost.setPreferredSize(knappDim);
		deleteUser.setPreferredSize(knappDim);
		editUser.setPreferredSize(knappDim);
		removePost.setPreferredSize(knappDim);
		updatePost.setPreferredSize(knappDim);

		button1.addActionListener(this);
		button1.setActionCommand("openCU");
		GUbutton.addActionListener(this);
		GUbutton.setActionCommand("getUsers");
		button2.addActionListener(this);
		button2.setActionCommand("openCP");
		button3.addActionListener(this);
		button3.setActionCommand("openLogin");
		manageUser.addActionListener(this);
		manageUser.setActionCommand("openMU");
		managePost.addActionListener(this);
		managePost.setActionCommand("openMP");

		CUbutton.addActionListener(this);
		CPbutton.addActionListener(this);

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
		MUWindow = new JFrame("Manage Users");
		Container MUcontent = MUWindow.getContentPane();
		JPanel pane = new JPanel();

		User[] users = um.getUsers(server);
		String[] columnNames = { "UserID", "Username", "Password", "Email",
				"User Type" };

		usersTable = new JTable(users.length, 5);
		usersTable.setModel(model);
		usersTable.setSelectionMode(0);

		for (int i = 0; i < 5; i++) {
			model.addColumn(columnNames[i]);
		}
		updateUsersPanel();

		MUWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MUWindow.setResizable(false);
		MUWindow.setLayout(lm);

		JScrollPane scrollPane = new JScrollPane(usersTable);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setAutoscrolls(true);

		resetConstraints();
		pane.add(scrollPane);
		c.insets = new Insets(5, 5, 5, 5);
		MUcontent.add(button1, c);
		c.gridy = 1;
		MUcontent.add(editUser, c);
		c.gridy = 2;
		MUcontent.add(deleteUser, c);
		c.gridy = 0;
		c.gridx = 1;
		c.gridheight = 5;
		MUcontent.add(pane, c);

		editUser.addActionListener(this);
		editUser.setActionCommand("openEU");
		deleteUser.addActionListener(this);
		deleteUser.setActionCommand("deleteUser");

		MUWindow.pack();
		MUWindow.setLocationRelativeTo(null);
		MUWindow.setVisible(true);

	}

	// uppdaterar tabellen med användare
	private void updateUsersPanel() {
		User[] users = um.getUsers(server);

		model.setRowCount(0);
		for (User user : users) {
			model.addRow(new Object[] { user.getUserId(), user.getUsername(),
					user.getPassword(), user.getEmail(), user.getUsertype() });
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
		CUcontent.add(textField1, c);
		c.gridy = 1;
		CUcontent.add(textField2, c);
		c.gridy = 2;
		CUcontent.add(textField3, c);
		c.gridy = 3;
		combobox.setSelectedIndex(1);
		CUcontent.add(combobox, c);

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
		EUcontent.add(textField1, c);
		c.gridy = 1;
		EUcontent.add(textField2, c);
		c.gridy = 2;
		EUcontent.add(textField3, c);
		c.gridy = 3;
		combobox.setSelectedIndex(1);
		EUcontent.add(combobox, c);

		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 0.1;
		c.weighty = 1;

		EUbutton.setActionCommand("editUser");
		EUcontent.add(EUbutton, c);

		textField1.setText(user.getUsername());
		textField2.setText(user.getPassword());
		textField3.setText(user.getEmail());
		combobox.setSelectedIndex(user.getUsertype());

		EUWindow.pack();
		EUWindow.setLocationRelativeTo(null);
		EUWindow.setVisible(true);
	}

	// hantera inlägg fönstret
	private void managePostsWindow() {
		MPWindow = new JFrame("Manage Post");
		Container MPcontent = MPWindow.getContentPane();
		resetConstraints();

		MPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MPWindow.setResizable(false);
		MPWindow.setLayout(lm);

		c.insets = new Insets(5, 5, 5, 5);
		MPcontent.add(button2, c);
		c.gridy = 1;
		MPcontent.add(removePost, c);
		c.gridy = 2;
		MPcontent.add(updatePost, c);
		c.gridy = 3;
		MPcontent.add(showPosts, c);

		showPosts.addActionListener(this);
		showPosts.setActionCommand("showPosts");

		MPWindow.pack();
		MPWindow.setLocationRelativeTo(null);
		MPWindow.setVisible(true);

	}

	// skapar fönstret för att göra inlägg
	private void createPostWindow() {
		CPWindow = new JFrame("Create Post");
		Container CPcontent = CPWindow.getContentPane();

		CPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CPWindow.setResizable(false);
		CPWindow.setLayout(lm);

		CPpostBox.setBorder(border);
		CPpostBox.setLineWrap(true);

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
		CPcontent.add(CPtitle, c);
		c.gridy = 1;
		CPcontent.add(CPpostBox, c);

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

	// skapar login fönster
	private void loginWindow() {
		loginWindow = new JFrame("Login");
		Container loginContent = loginWindow.getContentPane();

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

		if ("openEU".equals(e.getActionCommand()))
			editUserWindow(um.getUser(server, Integer.parseInt(usersTable.getModel().getValueAt(
					usersTable.getSelectedRow(), 0).toString())));

		if ("createUser".equals(e.getActionCommand())) {
			um.createUser(server, textField1.getText(), textField2.getText(),
					textField3.getText(), combobox.getSelectedIndex());
			CUWindow.dispose();
			textField1.setText("");
			textField2.setText("");
			textField3.setText("");
			JOptionPane.showMessageDialog(null, "User created!");
			updateUsersPanel();
		}
		
		if("editUser".equals(e.getActionCommand()))
			//TODO skapa

		if ("deleteUser".equals(e.getActionCommand())) {
			RemoveUser arg = new RemoveUser();
			arg.setUserId(Integer.parseInt(usersTable.getModel()
					.getValueAt(usersTable.getSelectedRow(), 0).toString()));

			try {
				server.removeUser(arg);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			updateUsersPanel();
		}

		if ("createPost".equals(e.getActionCommand())) {
			pm.createPost(server, CPtitle.getText(), CPpostBox.getText());
			CPWindow.dispose();
			CPtitle.setText("");
			CPpostBox.setText("");
			JOptionPane.showMessageDialog(null, "Post created!");
		}

		if ("login".equals(e.getActionCommand())) {
			session = login(loginUsername.getText(),
					loginPassword.getPassword());
			JOptionPane.showMessageDialog(null, session);
		}

		if ("showPosts".equals(e.getActionCommand())) {
			Post[] posts = pm.getPosts(server);
			JFrame test = new JFrame("test");
			test.setLayout(lm);
			JPanel pane = new JPanel();
			pane.setLayout(lm);

			JScrollPane scroll = new JScrollPane(pane);
			pane.setAutoscrolls(true);
			scroll.setPreferredSize(new Dimension(500, 700));
			scroll.getVerticalScrollBar().setUnitIncrement(10);
			test.add(scroll);

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

			test.pack();
			test.setLocationRelativeTo(null);
			test.setVisible(true);
		}

	}

}
