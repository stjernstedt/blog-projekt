package client;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.apache.axis2.AxisFault;

import core.CoreStub;
import core.CoreStub.CreateComment;
import core.CoreStub.CreatePost;
import core.CoreStub.CreateUser;
import core.CoreStub.GetUsers;
import core.CoreStub.GetUsersResponse;
import core.CoreStub.Login;
import core.CoreStub.LoginResponse;
import core.CoreStub.User;

public class Client implements ActionListener {

	private Logger logg = Logger.getLogger("Client Logger");
	private CoreStub server = null;
	private String session;

	private JFrame window = new JFrame("Blog Admin");
	private JFrame CPWindow;
	private JFrame CUWindow;
	private JFrame loginWindow;
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

	private JPasswordField loginPassword = new JPasswordField(15);

	private JTextArea usersBox = new JTextArea(10, 40);
	private JTextArea CPpostBox = new JTextArea(20, 80);

	private String[] choices = { "Admin", "User", "Guest" };
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox combobox = new JComboBox(choices);
	
	private JButton EditUser = new JButton("Edit User");
	private JButton EditPost = new JButton("Edit Post");
	private JButton button1 = new JButton("Create User");
	private JButton button2 = new JButton("Create Post");
	private JButton button3 = new JButton("Login");
	private JButton CUbutton = new JButton("Create User");
	private JButton GUbutton = new JButton("Get Users");
	private JButton CPbutton = new JButton("Post");
	private JButton loginButton = new JButton("Login");
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

		usersBox.setBorder(border);
		mainContent.setLayout(lm);
				
		mainContent.add(EditUser, c);
		c.gridy = 1;
		mainContent.add(EditPost, c);
		c.gridy = 0;
		c.gridx = 2;
		mainContent.add(button1, c);
		c.gridy = 1;
		mainContent.add(GUbutton, c);
		c.gridx = 1;
		c.gridy = 1;
		//mainContent.add(usersBox, c);
		mainContent.add(button2, c);
		c.gridx = 1;
		c.gridy = 0;
		mainContent.add(button3, c);
		
		
		
		button1.setPreferredSize(new Dimension(150, 25));
		button2.setPreferredSize(new Dimension(150, 25));
		button3.setPreferredSize(new Dimension(150, 25));
		GUbutton.setPreferredSize(new Dimension(150, 25));
		EditUser.setPreferredSize(new Dimension(150, 25));
		EditPost.setPreferredSize(new Dimension(150, 25));
		
		
		button1.addActionListener(this);
		button1.setActionCommand("openCU");
		GUbutton.addActionListener(this);
		GUbutton.setActionCommand("getUsers");
		button2.addActionListener(this);
		button2.setActionCommand("openCP");
		button3.addActionListener(this);
		button3.setActionCommand("openLogin");
		EditUser.addActionListener(this);
		EditUser.setActionCommand("openEU");

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

	private User[] getUsers() {
		GetUsers arg = new GetUsers();
		GetUsersResponse result = null;

		try {
			result = server.getUsers(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}

		return result.get_return();
	}

	private void createUser(String username, String password, String email,
			int usertype) {
		CreateUser user = new CreateUser();
		user.setEmail(email);
		user.setPassword(password);
		user.setUsername(username);
		user.setUsertype(usertype);

		try {
			server.createUser(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	private void createPost(String title, String text) {
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

	private void createComment(String email, String text, String name,
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
	
	private void editUserWindow() {
		EUWindow = new JFrame("Edit User");
		Container EUcontent = EUWindow.getContentPane();
		
		
		EUWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		EUWindow.setResizable(false);
		EUWindow.setLayout(lm);		
		
		
		EUWindow.pack();
		EUWindow.setVisible(true);
		
	}

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

	private void createPostWindow() {
		CPWindow = new JFrame("Create Post");
		Container CPcontent = CPWindow.getContentPane();

		CPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CPWindow.setResizable(false);
		CPWindow.setLayout(lm);

		CPpostBox.setBorder(border);

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
		// c.insets = new Insets(0, 0, 0, 50);
		c.weightx = 0.1;
		c.gridy = 2;
		CPcontent.add(CPbutton, c);

		CPbutton.setActionCommand("createPost");
		CPcontent.add(CPbutton, c);

		CPWindow.pack();
		CPWindow.setLocationRelativeTo(null);
		CPWindow.setVisible(true);
	}

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

	private void resetConstraints() {
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = 10;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("openCU".equals(e.getActionCommand()))
			createUserWindow();

		if ("openCP".equals(e.getActionCommand()))
			createPostWindow();

		if ("openLogin".equals(e.getActionCommand()))
			loginWindow();
		
		if ("openEU".equals(e.getActionCommand()))
			editUserWindow();

		if ("createUser".equals(e.getActionCommand())) {
			createUser(textField1.getText(), textField2.getText(),
					textField3.getText(), combobox.getSelectedIndex());
			CUWindow.dispose();
			textField1.setText("");
			textField2.setText("");
			textField3.setText("");
			JOptionPane.showMessageDialog(null, "User created!");
		}

		if ("createPost".equals(e.getActionCommand())) {
			createPost(CPtitle.getText(), CPpostBox.getText());
			CPWindow.dispose();
			CPtitle.setText("");
			CPpostBox.setText("");
			JOptionPane.showMessageDialog(null, "Post created!");
		}
		
		if ("login".equals(e.getActionCommand())){
			session = login(loginUsername.getText(), loginPassword.getPassword());
			JOptionPane.showMessageDialog(null, session);
		}

		if ("getUsers".equals(e.getActionCommand())) {
			User[] users = getUsers();
			String string = "";

			for (User user : users) {
				string = string + user.getUserId() + " " + user.getUsername()
						+ " " + user.getEmail() + "\n";
			}
			usersBox.setText(string);
		}
	}

}
