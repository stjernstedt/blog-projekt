package client;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.axis2.AxisFault;

import core.CoreStub;
import core.CoreStub.CreateComment;
import core.CoreStub.CreatePost;
import core.CoreStub.CreateUser;
import core.CoreStub.GetUsers;
import core.CoreStub.GetUsersResponse;
import core.CoreStub.Login;
import core.CoreStub.LoginResponse;
import core.CoreStub.Test;
import core.CoreStub.TestResponse;
import core.CoreStub.User;

public class Client implements ActionListener {

	private CoreStub server = null;

	private JFrame window = new JFrame("Blog Admin");
	private JFrame CPWindow;
	private JFrame CUWindow;

	private Container mainContent = window.getContentPane();
	private LayoutManager lm = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();

	private TextField textField1 = new TextField(15);
	private TextField textField2 = new TextField(15);
	private TextField textField3 = new TextField(15);
	private TextField CPtitle = new TextField(15);

	private TextArea usersBox = new TextArea(10, 40);
	private TextArea CPpostBox = new TextArea(20, 80);

	private String[] choices = { "Admin", "User", "Guest" };
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox combobox = new JComboBox(choices);

	private JButton button1 = new JButton("Create User");
	private JButton button2 = new JButton("Create Post");
	private JButton button3 = new JButton("Login");
	private JButton CUbutton = new JButton("Create User");
	private JButton GUbutton = new JButton("Get Users");
	private JButton CPbutton = new JButton("Post");

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
		window.setSize(600, 400);
		window.setLocationRelativeTo(null); // centrerar fönstret
		window.setResizable(false);

		mainContent.setLayout(lm);
		mainContent.add(button1);
		mainContent.add(GUbutton);
		mainContent.add(usersBox);
		mainContent.add(button2);
		mainContent.add(button3);

		button1.addActionListener(this);
		button1.setActionCommand("openCU");
		GUbutton.addActionListener(this);
		GUbutton.setActionCommand("getUsers");
		button2.addActionListener(this);
		button2.setActionCommand("openCP");
		button3.addActionListener(this);
		button3.setActionCommand("login");

		CUbutton.addActionListener(this);
		CPbutton.addActionListener(this);

		// window.pack();
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

	private String login(String username, String password) {
		Login arg = new Login();
		LoginResponse result = null;
		arg.setUsername(username);
		arg.setPassword(password);

		try {
			result = server.login(arg);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-2);
		}
		return result.get_return().toString();
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
		//post.setDate(date);
		post.setText(text);
		post.setTitle(title);
		//post.setUserId(userId);

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

	private void createUserWindow() {
		CUWindow = new JFrame("Create User");
		Container CUcontent = CUWindow.getContentPane();

		JLabel label1 = new JLabel("Username:");
		JLabel label2 = new JLabel("Password:");
		JLabel label3 = new JLabel("Email:");
		JLabel label4 = new JLabel("Usertype:");

		CUWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CUWindow.setSize(400, 200);
		CUWindow.setLocationRelativeTo(null);
		CUWindow.setResizable(false);
		CUWindow.setLayout(lm);

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

		CUWindow.setVisible(true);
	}

	private void createPostWindow() {
		CPWindow = new JFrame("Create Post");
		Container CPcontent = CPWindow.getContentPane();

		CPWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CPWindow.setSize(1000, 500);
		CPWindow.setLocationRelativeTo(null);
		CPWindow.setResizable(false);
		CPWindow.setLayout(lm);

		JLabel CPlabel1 = new JLabel("Title");
		JLabel CPlabel2 = new JLabel("Text");

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

		c.gridy = 2;
		CPcontent.add(CPbutton, c);
		
		CPbutton.setActionCommand("createPost");
		CPcontent.add(CPbutton, c);

		CPWindow.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("openCU".equals(e.getActionCommand()))
			createUserWindow();

		if ("openCP".equals(e.getActionCommand()))
			createPostWindow();

		if ("login".equals(e.getActionCommand()))
			JOptionPane.showMessageDialog(null, login("kalle", "kalle"));

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
			JOptionPane.showMessageDialog(null, "Post created!");		}
		

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
