package DB_GUI;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class guiForDB extends dbCon {
    
	
	//class instances for Login
	private static Connection con;
	
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	  
	private JFrame frame;
	public JTextField usernameField;
	public JPasswordField passwordField;
	private JLabel login;
	private JComboBox<Object> userChoice;
	private JLabel usrLabel;
	private JLabel psdLabel;
	private JButton doneButton;
	
	//these babies are important
	private static String usernameLogin;
	private static String passwordLogin;
	
	public static final int MYSQL_DUPLICATE_PK = 1062;

	
	
	
	//constructor of guiForDB
	public guiForDB() {
		createLoginGui();
		createEvents();
	}
	
	public static String getUsername() {return usernameLogin;}
	public static String getPassword() {return passwordLogin;}
	
	public JFrame getFrame() {return frame;}
	

	 
	 
	//method for username/password validation and second gui pop up decision maker
 	private void validateLogin() {
			int index;
			
			   try {
				   
			   //establishes connection
			    con = connectToDB();
			  
			   String query[] = {
			    "SELECT username,password FROM user WHERE username = ? AND password = ?;",
				"SELECT u.username,u.password FROM user u JOIN manager m ON u.username = m.man_username WHERE u.username = ? AND u.password = ?;",
			    "SELECT u.username,u.password FROM user u JOIN evaluator e ON u.username = e.ev_username WHERE u.username = ? AND u.password = ?;",
			    "SELECT u.username,u.password FROM user u JOIN employee e ON u.username = e.username  WHERE u.username = ? AND u.password = ?;"
			                    };
			   
			   index = userChoice.getSelectedIndex();
			   
			   pst = con.prepareStatement(query[index - 1 ]);
			   
			   pst.setString(1,usernameField.getText());
			   pst.setString(2,String.valueOf(passwordField.getPassword()));
			   
			   rs = pst.executeQuery();
			   
			     if(rs.next()) {
			    	 
			    	 //this is super important. gets and stores the value of username into usernameLogin
			    	 usernameLogin = usernameField.getText();
			    	 passwordLogin = String.valueOf(passwordField.getPassword());
			    	 
			    	 JOptionPane.showMessageDialog(null, "Username and Password are correct.\nWelcome!");
			    	 
			    	 //inner if for frame selection
			    	 if (userChoice.getSelectedItem() == "Administrartor") {
			    	     frame.dispose();
			    	     AdminGUI adm = new AdminGUI();
			    	     adm.setVisible(true);
			    	     
			    	     }
			    	 
			    	 else if (userChoice.getSelectedItem() == "Manager") {
			    		 frame.dispose();
			    		 ManagerGUI mg = new ManagerGUI();
			    		 mg.setVisible(true);
			    	     }
			    	 
			         else if (userChoice.getSelectedItem() == "Evaluator") {
			        	 frame.dispose();
			        	 EvaluatorGUI eg = new EvaluatorGUI(); 
			        	 eg.setVisible(true);
			        	 
			            }
			        	 
			         else if (userChoice.getSelectedItem() == "Employee") {
			        	frame.dispose();
			        	EmplGUI eg = new EmplGUI();
			        	eg.setVisible(true);
			            }
			     
			         // end of inner if
			    	 
			     }
			     
			    else {
			    	 JOptionPane.showMessageDialog(null, "Username or password is incorrect");
			    	 usernameField.setText("");
			    	 passwordField.setText("");  
			    	 }
			     con.close();
			   }
			   
			   catch(ArrayIndexOutOfBoundsException e) {JOptionPane.showMessageDialog(null, "Invalid Option. Select user Category.");}
			   catch(Exception e) {JOptionPane.showMessageDialog(null, e);}
		   }   
 	
 	/*//method which logs u out
 	 public static void logOut(JButton btn) {
 		int a = JOptionPane.showConfirmDialog(btn,"Are you sure?");
 		  if(a == JOptionPane.YES_OPTION) {
 			   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE;)
 			  
 			    guiForDB window = new guiForDB();
				window.frame.setVisible(true);
 		  }
 		 
 	 }*/
 	
 	      
		
		
	// Initialize the contents of the frame.
	private void createLoginGui() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1436, 637);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Font font1 = new Font("Arial",Font.ITALIC,24);
	    Font font2 = new Font("Arial",Font.BOLD | Font.ITALIC,30);
		
		login = new JLabel("Log in as:");
		login.setBounds(328, 66, 189, 40);
		login.setFont(font2);
		
		String select [] = {"Choose","Administrartor", "Manager", "Evaluator", "Employee"};
		userChoice = new JComboBox<Object>(select);
		userChoice.setBounds(547, 57, 258, 62);
		userChoice.setFont(font1);
		
		usrLabel = new JLabel("Username:");
		usrLabel.setBounds(323, 218, 173, 44);
		usrLabel.setFont(font2);
		
		psdLabel = new JLabel("Password:");
		psdLabel.setBounds(328, 338, 152, 36);
		psdLabel.setFont(font2);
		
		doneButton = new JButton("Done!");
		doneButton.setBounds(521, 475, 164, 63);
		doneButton.setFont(font2);
		
		usernameField = new JTextField();
		usernameField.setBounds(553, 218, 252, 48);
		usernameField.setFont(font1);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(553, 334, 252, 48);
		passwordField.setFont(font1);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(login);
		frame.getContentPane().add(psdLabel);
		frame.getContentPane().add(usrLabel);
		frame.getContentPane().add(passwordField);
		frame.getContentPane().add(usernameField);
		frame.getContentPane().add(userChoice);
		frame.getContentPane().add(doneButton);
		
		
	}

	
	//method wich contains all events
    private void createEvents() {
	
	doneButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			validateLogin();
		}
	});
		
}
	
    
   	  
    public static void main(String[] args) throws SQLException {	    	   	    	
    	
    	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					guiForDB window = new guiForDB();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}  
			}
		});  
		
      		
		
	}//end of main method

}// end of class guiForDB
