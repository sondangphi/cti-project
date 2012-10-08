package org.asterisk.main;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.asterisk.model.QueueObject;
import org.asterisk.utility.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JMenuBar;

public class loginForm {

	private JFrame frmLoginFormAgent;
	private JTextField textAgent;
	private JTextField textExtension;
	private JPasswordField passwordField;
	private JLabel lblAgentUser;
	private JLabel lblPassword;
	private JLabel lblExtension;
	private JLabel lblQueue;
	public static JLabel lblStatus;
	private JButton btnClear;
	
	private Agent agentClient ;
	private static String host = "localhost";
	private static int port2 = 33333;
	private static int port1 = 22222;
	private static QueueObject qObject;
	private static Socket client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginForm window = new loginForm();
					window.frmLoginFormAgent.setVisible(true);		
					client = new Socket(host, port2);
					InputStream is = client.getInputStream();  			
					ObjectInputStream ois = new ObjectInputStream(is);  
					qObject = (QueueObject)ois.readObject();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public loginForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoginFormAgent = new JFrame();
		frmLoginFormAgent.setResizable(false);
		frmLoginFormAgent.setTitle("Login Form Agent");
		frmLoginFormAgent.setBounds(100, 100, 491, 375);
		frmLoginFormAgent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String agent = textAgent.getText();
				String pass = passwordField.getText();
				String exten = textExtension.getText();
//				String queue = textQueue.getText();
				String command = "100@"+agent+"@"+pass+"@SIP/"+exten;
				agentClient = new Agent(host, port1, command);
				lblStatus.setText(command);
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		textAgent = new JTextField();
		textAgent.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textAgent.setColumns(10);
		
		textExtension = new JTextField();
		textExtension.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textExtension.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		lblAgentUser = new JLabel("Agent User");
		lblAgentUser.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lblPassword = new JLabel("Password\r\n");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lblExtension = new JLabel("Extension");
		lblExtension.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lblQueue = new JLabel("Queue");
		lblQueue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lblStatus = new JLabel("status");
		
		btnClear = new JButton("Logout");
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				textAgent.setText("");
//				textExtension.setText("");
//				textQueue.setText("");
//				passwordField.setText("");
				String agent = textAgent.getText();
				String pass = passwordField.getText();
				String exten = textExtension.getText();
//				String queue = textQueue.getText();
				String command = "102"+"@SIP/"+exten+"@";
				try {
					agentClient.sendtoServer(command);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				lblStatus.setText(command);
				
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JList list = new JList();
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//mouse click event on combobox
			}
		});
		comboBox_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {//event change item
				
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"queue1", "queue2", "queue3", "queue4", "queue5", "queue6"}));
		GroupLayout groupLayout = new GroupLayout(frmLoginFormAgent.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
					.addGap(40))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblPassword, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblAgentUser, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
								.addComponent(lblExtension, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblQueue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addGap(119)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnClear)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(comboBox_1, 0, 133, Short.MAX_VALUE)
								.addComponent(passwordField, Alignment.LEADING, 133, 133, Short.MAX_VALUE)
								.addComponent(textAgent, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
								.addComponent(textExtension, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
							.addGap(106))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStatus)
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(textAgent, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAgentUser))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(lblExtension))
						.addComponent(textExtension, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblQueue))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(41)
					.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		frmLoginFormAgent.getContentPane().setLayout(groupLayout);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
