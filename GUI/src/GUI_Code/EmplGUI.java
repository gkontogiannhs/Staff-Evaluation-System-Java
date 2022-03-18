package DB_GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import net.proteanit.sql.DbUtils;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

public class EmplGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	 //connection object
    private Connection connection = null;
    private String psd,bio;
    
    private static String usr = guiForDB.getUsername();
	
    
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;
	
	private JPanel contentPane,ProfTab,JobTab,More;
	private JTextField username;
	private JLabel lbUsername, lblOldPassword, lblNewPassword, lbChange,BiosLabel,lbAvailableJobs,lblApplayToJobs,lblinterview;
	private JButton btnChangePas,UpBiosBtn,btnDeleteApplication,btnApplayJob;
	private JRadioButton rdbtnShowOldPas, rdbtnShowNewPas,rdbtnShPasList,rdbtnShowBio;
	private JPasswordField OldPasField, NewPasField;
	private JTabbedPane Tab;
	private JList<Object> Userlist,EmplList,Languagelist;
	private JTextPane BiosTextPanel,InterviewPanel;
	private JTable Jobstable,AppliedJobsTable;
	private JLabel lblAppliedJobs;
	private JLabel lblDeleteJob;
	private JComboBox<Object> DeleteJobBox,JobIdBox,UpdateAppBox;
	private JLabel lblSelectJob,lbljobId,lblUpdateInterview,lblUpdateApplication,lblNewInterview;
	private JTextPane NewInterview;
	private JButton btnUpdateApp;
	private JLabel lblProject,lblNewProject,lblProjectUrl;
	private JTable Projecttable;
	private JLabel lblNewProjectName;
	private JTextField ProjectNameField;
	private JTextField ProjectUrlField;
	private JButton btnAddProject,btnAddDegree,btnSubmit;
	private JLabel lblDegree;
	private JTable Degreetable;
	private JLabel lblAddDegree;
	private JTextField TitleField,InstituteField,Grade_yearField,GradeField;
	private JLabel lblTitle,lblInstitute,lblGrad_year,lblGrade,lblLevel;
	private JComboBox Level,LangcomboBox,instComboBox,titleComboBox;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	
	
	
	public EmplGUI() {
		createEmplGUI();
		EmplEvents();
	}
	
	
	
	//For Profile Tab
	//change user pas
	private void changeUserPas() {
		
		 try {
			 String query = "UPDATE user SET password = ? WHERE username = ?  AND password = ?";
			 pst = connection.prepareStatement(query);
			 
			 pst.setString(1,String.valueOf(NewPasField.getPassword()));
			 pst.setString(2,username.getText());
			 pst.setString(3,String.valueOf(OldPasField.getPassword()));
			 
			 pst.execute();
			 
			 JOptionPane.showMessageDialog(null, "Successfully Changed");
			 
			 pst.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		 finally {
			 loadUserStatus();
			 NewPasField.setText("");
			 username.setText("");
			 OldPasField.setText("");
		 }
		}
	
	
	
	//Show Bio
	public void ShowBio() {
		 try {
			 String query = "SELECT bio FROM employee WHERE username = ? ";
				pst = connection.prepareStatement(query);
				pst.setString(1, usr);
				rs = pst.executeQuery();
			 
				while(rs.next()) {
					bio = rs.getString("bio");
				}
				
			 JOptionPane.showMessageDialog(null,bio);
			 
			 pst.close();
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
	}
	
	
	//change Bios
	private void ChangeBios() {
		 try {
			 String query = "{call changeBios(?,?)}";
			 cs = connection.prepareCall(query);
				
			 if(BiosTextPanel.getText().isEmpty())
			 {
			    JOptionPane.showMessageDialog(null, "Cant Be Empty");
			 }
			 else {
			   cs.setString(1, usr);
			   cs.setString(2,BiosTextPanel.getText());
			 
			   cs.execute();
			 
			   cs.close();
			   JOptionPane.showMessageDialog(null, "Successful Changed");
			 //connection.close();
			 }
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		 loadEmplStatus();
		}
	 
	
	
	//loads the user profile data for specific user
	private void loadUserStatus() {
		try {
			String query = "SELECT * FROM user WHERE username = ? ";
			pst = connection.prepareStatement(query);
			pst.setString(1, usr);
			rs = pst.executeQuery();
			
            DefaultListModel<Object> dlm = new DefaultListModel<Object>();
			
            while(rs.next())
            {
			 dlm.addElement("Username:  "+rs.getString("username")); // can also use usr variable
		     psd = rs.getString("password");
			 dlm.addElement("Password:  Press button to see password.");
			 dlm.addElement("Name:  "+rs.getString("name"));
			 dlm.addElement("Surname:  "+rs.getString("surname"));
			 dlm.addElement("Register Date:  "+rs.getString("register_date"));
			 dlm.addElement("Email:  "+rs.getString("email"));
             }
            
			Userlist.setModel(dlm);
			
			pst.close();
			rs.close();
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	

	//loads the employee data
	private void loadEmplStatus() {
		try {
			String query = "SELECT * FROM employee WHERE username = ? ";
			pst = connection.prepareStatement(query);
			pst.setString(1, usr);
			rs = pst.executeQuery();
			
            DefaultListModel<Object> dlm = new DefaultListModel<Object>();
            while(rs.next())
            {
            	dlm.addElement("Comp AFM: "+rs.getString("comp_afm"));
            	dlm.addElement("Exp Years: "+rs.getString("exp_years"));
            	dlm.addElement("Certificates: "+rs.getString("certificates"));
            	dlm.addElement("Awards: "+rs.getString("awards"));
            	dlm.addElement("Reference: "+rs.getString("reference"));	
            }
        
            EmplList.setModel(dlm);
            
	}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	
	//Load Language
	public void loadLanguage() {
		try {
			String query = "SELECT * FROM languages WHERE us_employee = ? ";
			pst = connection.prepareStatement(query);
			pst.setString(1, usr);
			rs = pst.executeQuery();
			
            DefaultListModel<Object> dlm = new DefaultListModel<Object>();
            
            while(rs.next())
            {
              dlm.addElement("Languages:"+rs.getString("lang"));		
            }
            
            Languagelist.setModel(dlm);
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	
	//For Jobs Tab
	//Load Available Jobs
	public void loadAvailJobs() {
		try {
			
			 String stored = "{call ShowJobs(?)}";
			 
			  cs = connection.prepareCall(stored);
			  cs.setString(1,usr);
			 
			  rs = cs.executeQuery(); // get data
			  
			  Jobstable.setModel(DbUtils.resultSetToTableModel(rs)); // load them on table
			  
			  JTableHeader header = Jobstable.getTableHeader();                   // make it look nice
			  Jobstable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		      header.setBackground(Color.gray);
		      Jobstable.setFont(new Font("Arial",Font.ITALIC,14));
		      
		      rs.close();
			  cs.close();
			
		}
		catch (Exception ex) {ex.printStackTrace();}
	}
	
	
	//fill JobId combo box
	public void fillJobIdBox() {
		try {
			
			 String stored = "{call ShowJobs(?)}";
			 cs = connection.prepareCall(stored);
			 
			 cs.setString(1,usr);
			 
			 rs = cs.executeQuery();
			 JobIdBox.addItem("");
			 
			 while(rs.next())
			 {
				 JobIdBox.addItem(rs.getString("code"));
			 }
			 
			 cs.close(); 
			 
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
	}
	
	
	//Apply for a Job
	public void RequestEval() {
		try {
			 String stored = "{call RequestEval(?,?,?)}";
			 cs = connection.prepareCall(stored);
			 
			 cs.setString(1, usr);
			 cs.setString(2,JobIdBox.getSelectedItem().toString());
			 cs.setString(3,InterviewPanel.getText());
			 
			 cs.execute();
			 
			 cs.close();
			 JOptionPane.showMessageDialog(null, "Successfuly Applied");
			 
		}
		catch(SQLException e){
		    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
		    	JOptionPane.showMessageDialog(null,"This is already a register.");
		      }
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
		loadAppliedJobs();
		fillJobCodesComboBox();
	}
	
	
	//Load Applied Jobs
	public void loadAppliedJobs() {
		try {
			String stored = "{call ShowAppliedJobs(?)}";
			 cs = connection.prepareCall(stored);
			 
			 cs.setString(1, usr);
			 
			  rs = cs.executeQuery();
			  AppliedJobsTable.setModel(DbUtils.resultSetToTableModel(rs));
			  JTableHeader header = AppliedJobsTable.getTableHeader();
			  AppliedJobsTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		      header.setBackground(Color.gray);
		      AppliedJobsTable.setFont(new Font("Arial",Font.ITALIC,14));
		      
		      rs.close();
			  cs.close();
			
		}
		catch (Exception ex) {ex.printStackTrace();}
	}
	
	

	//fills a combo box with all the job ids 
	private void fillJobCodesComboBox() {
		try {
			 
			 String query = "SELECT job_code FROM request_evaluation WHERE empl_username = ?";
			 pst = connection.prepareStatement(query);
			 
		     pst.setString(1, usr);
		     
			 rs = pst.executeQuery();
			 
			 DeleteJobBox.removeAllItems();
			 UpdateAppBox.removeAllItems();
			 
			 DeleteJobBox.addItem("");
			 UpdateAppBox.addItem("");
			 
			 while(rs.next())
			 {
				 DeleteJobBox.addItem(rs.getString("job_code"));
				 UpdateAppBox.addItem(rs.getString("job_code"));
			 }
			 
			 pst.close();
			 rs.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }	
		
	}
	
	
	//Delete Job Application
	public void DeleteJobApplication() {
		try {
			String stored = "{call DeleteAppliedJobs(?,?)}";
			 cs = connection.prepareCall(stored);
			 
			 cs.setString(1, usr);
			 cs.setInt(2,Integer.parseInt(DeleteJobBox.getSelectedItem().toString()));
			 
             cs.execute();
			 
			 cs.close();
			 
		}catch(Exception e) { JOptionPane.showMessageDialog(null, e);}
		loadAppliedJobs();
		fillJobCodesComboBox();
	}
	
	
	//Update Job Application
	public void UpdateApplication() {
		try {
		String stored = "{call UpdateApplication(?,?,?)}";
		 cs = connection.prepareCall(stored);
		 
		 cs.setString(1, usr);
		 cs.setInt(2,Integer.parseInt(UpdateAppBox.getSelectedItem().toString()));
		 cs.setString(3,NewInterview.getText());
		 
         cs.execute();
		 
		 cs.close();
		 JOptionPane.showMessageDialog(null, "Successfuly Updated");
		 
	}catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
	 finally {
		loadAppliedJobs();
		fillJobCodesComboBox();
		NewInterview.setText("");}
	}
	
	
	//For More Tab
	// Shows Projects for each user
	public void loadProject() {
		try {
			String stored = "{call ShowProject(?)}";
            CallableStatement cp = connection.prepareCall(stored);
			
             cp.setString(1,usr);
            
			  ResultSet rs = cp.executeQuery();
			  Projecttable.setModel(DbUtils.resultSetToTableModel(rs));
			  JTableHeader header = Projecttable.getTableHeader();
			  Projecttable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		      header.setBackground(Color.gray);
		      Projecttable.setFont(new Font("Arial",Font.ITALIC,14));
		      
		      rs.close();
			  cp.close();
			
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
	}
	 
	
	//Add Project
	public void AddProject() {
		try {
			String stored = "{call createNewEmplProject(?,?,?)}";
            CallableStatement cp = connection.prepareCall(stored);
			
            cp.setString(1,usr);
            cp.setString(2,ProjectNameField.getText());
            cp.setString(3,ProjectUrlField.getText());
            
            cp.execute();
            
            cp.close();
            JOptionPane.showMessageDialog(null, "Successfuly Added");
   		 //connection.close();
            
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
		finally{
			loadProject();
			ProjectNameField.setText("");
			ProjectUrlField.setText("");
		}
		
	}
	
	
	//Add Lang
	public void AddLang() {
		try {
			String stored = "{call createNewEmplLan(?,?)}";
            CallableStatement cp = connection.prepareCall(stored);
			
            cp.setString(1,usr);
            cp.setString(2,LangcomboBox.getSelectedItem().toString());
            
            cp.execute();
            
            cp.close();
			
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
		loadLanguage();
		
	}
	
	
	//load Degree
	public void loadDegree() {
		try {
			String stored = "{call ShowDegree(?)}";
            CallableStatement cp = connection.prepareCall(stored);
			
             cp.setString(1,usr);
            
			  ResultSet rs = cp.executeQuery();
			  Degreetable.setModel(DbUtils.resultSetToTableModel(rs));
			  JTableHeader header = Degreetable.getTableHeader();
			  Degreetable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		      header.setBackground(Color.gray);
		      Degreetable.setFont(new Font("Arial", Font.ITALIC, 14));
		      
		      rs.close();
			  cp.close();
			
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
	}
	
	
	//Add Degree
   public void AddDegree() {
	   try {
		   String stored = "{call createNewEmplDegree(?,?,?,?,?,?)}";
           CallableStatement cp = connection.prepareCall(stored);
			
           cp.setString(1,TitleField.getText());
           cp.setString(2,InstituteField.getText());
           cp.setString(3,usr);
           cp.setInt(4,Integer.parseInt(Grade_yearField.getText()));
           cp.setFloat(5,Float.parseFloat(GradeField.getText()));
           cp.setString(6,Level.getSelectedItem().toString());
           
           cp.execute();
           
           cp.close();
           
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, e); }
	   finally { TitleField.setText("");
	             InstituteField.setText("");
	             Grade_yearField.setText("");
	             GradeField.setText("");
	             Level.setSelectedIndex(0);
	             titleComboBox.setSelectedIndex(0);
	             instComboBox.setSelectedIndex(0);
	             loadDegree();
	             }
   }
   
   
   //Fill combo box with Titles
   private void fillTitleBox() {
	   try {
			 
			 String query = "SELECT DISTINCT title FROM degree";
			 PreparedStatement pst = connection.prepareStatement(query);
			 
		     
			 ResultSet rs = pst.executeQuery();
			 
			 titleComboBox.addItem("");

			 while(rs.next())
			 {
				 titleComboBox.addItem(rs.getString("title"));
			 }
			 
			 pst.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
	   
	   
   }
   
   
   //Fill combo box with Institutes
   private void fillInstBox() {
	   try {
			 
			 String query = "SELECT DISTINCT institute FROM degree";
			 PreparedStatement pst = connection.prepareStatement(query);
			 
		     
			 ResultSet rs = pst.executeQuery();
			 
			 instComboBox.addItem("");

			 while(rs.next())
			 {
				 instComboBox.addItem(rs.getString("institute"));
			 }
			 
			 pst.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
   }
	
	
	
	public  void createEmplGUI() {
		
		connection = dbCon.connectToDB();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1448, 677);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		Font font1 = new Font("Arial",Font.ITALIC,20);
		Font font2 = new Font("Arial", Font.ITALIC, 24);
		
		Tab = new JTabbedPane(JTabbedPane.TOP);
		Tab.setBounds(10, 0, 1486, 630);
		contentPane.add(Tab);
		
		//Component for Profile Tab
		ProfTab = new JPanel();
		Tab.addTab("Profile", null, ProfTab, null);
		
		btnChangePas = new JButton("Change");
		btnChangePas.setBounds(977, 219, 145, 51);
		ProfTab.add(btnChangePas);
		btnChangePas.setFont(font1);
		
		lblNewPassword = new JLabel("New Password:");
		lblNewPassword.setBounds(863, 148, 145, 29);
		ProfTab.add(lblNewPassword);
		lblNewPassword.setFont(font1);
		
		NewPasField = new JPasswordField();
		NewPasField.setBounds(1012, 147, 210, 29);
		ProfTab.add(NewPasField);
		NewPasField.setFont(font1);
		
		rdbtnShowNewPas = new JRadioButton("Reveal");
		rdbtnShowNewPas.setBounds(1241, 155, 105, 21);
		ProfTab.add(rdbtnShowNewPas);
		
		rdbtnShowOldPas = new JRadioButton("Reveal");
		rdbtnShowOldPas.setBounds(1241, 116, 105, 21);
		ProfTab.add(rdbtnShowOldPas);
		
		lblOldPassword = new JLabel("Old Password:");
		lblOldPassword.setBounds(863, 110, 137, 29);
		ProfTab.add(lblOldPassword);
		lblOldPassword.setFont(font1);
		
		OldPasField = new JPasswordField();
		OldPasField.setBounds(1012, 108, 210, 29);
		ProfTab.add(OldPasField);
		OldPasField.setFont(font1);
		
		lbUsername = new JLabel("Username:");
		lbUsername.setBounds(863, 70, 113, 29);
		ProfTab.add(lbUsername);
		lbUsername.setFont(font1);
			
		username = new JTextField();
		username.setBounds(1012, 69, 210, 29);
		ProfTab.add(username);
		username.setFont(font1);
		username.setColumns(10);
			
		lbChange = new JLabel("Change Password");
		lbChange.setBounds(859, 13, 204, 29);
		ProfTab.add(lbChange);
		lbChange.setFont(new Font("Arial", Font.ITALIC, 24));
		
		Userlist = new JList<Object>();
		Userlist.setBounds(10, 10, 532, 178);
		Userlist.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        Userlist.setBackground(Color.LIGHT_GRAY);
		ProfTab.add(Userlist);
		
		EmplList = new JList<Object>();
		EmplList.setBounds(10, 199, 532, 155);
		EmplList.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        EmplList.setBackground(Color.LIGHT_GRAY);
		ProfTab.add(EmplList);
		
		BiosLabel = new JLabel("Update Bio");
		BiosLabel.setBounds(863, 311, 132, 41);
		BiosLabel.setFont(font2);
		ProfTab.add(BiosLabel);
		
		BiosTextPanel = new JTextPane();
		BiosTextPanel.setFont(font1);
		BiosTextPanel.setBounds(859, 354, 455, 132);
		ProfTab.add(BiosTextPanel);
		
		UpBiosBtn = new JButton("Update");
		UpBiosBtn.setBounds(977, 516, 145, 51);
		UpBiosBtn.setFont(font1);
		ProfTab.add(UpBiosBtn);
		
		rdbtnShPasList = new JRadioButton("Show Pas");
		rdbtnShPasList.setBounds(548, 38, 105, 21);
		ProfTab.add(rdbtnShPasList);
		
		//Component for More 
			
	    More = new JPanel();
		Tab.addTab("Skills", null, More, null);
		More.setLayout(null);
		
		lblProject = new JLabel("Projects");
		lblProject.setBounds(209, 0, 98, 35);
		lblProject.setFont(font2);
		More.add(lblProject);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 36, 599, 107);
		More.add(scrollPane_1);
		
		Projecttable = new JTable();
		scrollPane_1.setViewportView(Projecttable);
		Projecttable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblNewProject = new JLabel("Submit New Project");
		lblNewProject.setBounds(63, 216, 237, 35);
		lblNewProject.setFont(font2);
		More.add(lblNewProject);
		
		lblNewProjectName = new JLabel("Description:");
		lblNewProjectName.setFont(font1);
		lblNewProjectName.setBounds(12, 280, 114, 26);
		More.add(lblNewProjectName);
		
		ProjectNameField = new JTextField();
		ProjectNameField.setFont(font1);
		ProjectNameField.setBounds(138, 276, 201, 35);
		More.add(ProjectNameField);
		ProjectNameField.setColumns(10);
		
		lblProjectUrl = new JLabel("URL:");
		lblProjectUrl.setFont(font1);
		lblProjectUrl.setBounds(12, 339, 52, 24);
		More.add(lblProjectUrl);
		
		ProjectUrlField = new JTextField();
		ProjectUrlField.setFont(font1);
		ProjectUrlField.setBounds(138, 335, 201, 33);
		More.add(ProjectUrlField);
		ProjectUrlField.setColumns(10);
		
		btnAddProject = new JButton("Sumbit");
		btnAddProject.setBounds(95, 414, 114, 45);
		btnAddProject.setFont(font2);
		More.add(btnAddProject);
		
		
		lblDegree = new JLabel("Degrees");
		lblDegree.setFont(font2);
		lblDegree.setBounds(1036, 0, 91, 35);
		More.add(lblDegree);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(771, 36, 637, 107);
		More.add(scrollPane_2);
		
		Degreetable = new JTable();
		scrollPane_2.setViewportView(Degreetable);
		Degreetable.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		lblAddDegree = new JLabel("Sumbit New Degree");
		lblAddDegree.setFont(font2);
		lblAddDegree.setBounds(973, 203, 230, 33);
		More.add(lblAddDegree);
		
		TitleField = new JTextField();
		TitleField.setFont(font1);
		TitleField.setBounds(890, 262, 237, 35);
		More.add(TitleField);
		TitleField.setColumns(10);
		
		InstituteField = new JTextField();
		InstituteField.setFont(font1);
		InstituteField.setColumns(10);
		InstituteField.setBounds(890, 313, 237, 35);
		More.add(InstituteField);
		
		Grade_yearField = new JTextField();
		Grade_yearField.setFont(font1);
		Grade_yearField.setColumns(10);
		Grade_yearField.setBounds(890, 409, 237, 39);
		More.add(Grade_yearField);
		
		GradeField = new JTextField();
		GradeField.setFont(font1);
		GradeField.setColumns(10);
		GradeField.setBounds(890, 461, 237, 35);
		More.add(GradeField);
		
		btnAddDegree = new JButton("Submit");
		btnAddDegree.setFont(font2);
		btnAddDegree.setBounds(1052, 532, 124, 45);
		More.add(btnAddDegree);
		
		lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Arial", Font.ITALIC, 20));
		lblTitle.setBounds(783, 264, 59, 31);
		More.add(lblTitle);
		
		lblInstitute = new JLabel("Institute:");
		lblInstitute.setFont(font1);
		lblInstitute.setBounds(783, 313, 98, 35);
		More.add(lblInstitute);
		
		lblGrad_year = new JLabel("Graduated:");
		lblGrad_year.setFont(font1);
		lblGrad_year.setBounds(776, 414, 102, 24);
		More.add(lblGrad_year);
		
		lblGrade = new JLabel("Grade:");
		lblGrade.setFont(font1);
		lblGrade.setBounds(783, 461, 70, 24);
		More.add(lblGrade);
		
		lblLevel = new JLabel("Level:");
		lblLevel.setFont(font1);
		lblLevel.setBounds(783, 361, 70, 24);
		More.add(lblLevel);
		
		String[] lev = {"","LYKEIO","IEK","TEI","BSc","MSc","PHD"};
		Level = new JComboBox(lev);
		Level.setFont(font1);
		Level.setBounds(890, 361, 240, 35);
		More.add(Level);
		
		instComboBox = new JComboBox();
		instComboBox.setFont(new Font("Arial", Font.ITALIC, 20));
		instComboBox.setBounds(1155, 312, 237, 36);
		More.add(instComboBox);
		
	    titleComboBox = new JComboBox();
		titleComboBox.setFont(new Font("Arial", Font.ITALIC, 20));
		titleComboBox.setBounds(1155, 261, 237, 36);
		More.add(titleComboBox);
		
		
		//Components of Job Tab
		JobTab = new JPanel();
		Tab.addTab("Jobs", null, JobTab, null);
		JobTab.setLayout(null);
		
	    lbAvailableJobs = new JLabel("Available Jobs");
		lbAvailableJobs.setBounds(234, 10, 183, 24);
		lbAvailableJobs.setFont(font2);
		JobTab.add(lbAvailableJobs);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 704, 123);
		JobTab.add(scrollPane);
		
		Jobstable = new JTable();
		scrollPane.setViewportView(Jobstable);
		Jobstable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblAppliedJobs = new JLabel("Applied Jobs");
		lblAppliedJobs.setBounds(975, 10, 146, 24);
		lblAppliedJobs.setFont(font2);
		JobTab.add(lblAppliedJobs);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(832, 45, 554, 123);
		JobTab.add(scrollPane_3);
		
		AppliedJobsTable = new JTable();
		scrollPane_3.setViewportView(AppliedJobsTable);
		AppliedJobsTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblDeleteJob = new JLabel("Delete Application");
		lblDeleteJob.setBounds(841, 201, 207, 30);
		lblDeleteJob.setFont(font2);
		JobTab.add(lblDeleteJob);
		
		DeleteJobBox = new JComboBox<Object>();
		DeleteJobBox.setFont(font1);
		DeleteJobBox.setBounds(975, 244, 51, 34);
		JobTab.add(DeleteJobBox);
		
		lblSelectJob = new JLabel("Select Job Id:");
		lblSelectJob.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSelectJob.setBounds(841, 247, 122, 28);
		JobTab.add(lblSelectJob);
		
		btnDeleteApplication = new JButton("Delete");
		btnDeleteApplication.setBounds(1145, 238, 123, 52);
		btnDeleteApplication.setFont(font1);
		JobTab.add(btnDeleteApplication);
		
		lblApplayToJobs = new JLabel("Apply Evaluation Request For Job");
		lblApplayToJobs.setBounds(10, 274, 391, 45);
		lblApplayToJobs.setFont(font2);
		JobTab.add(lblApplayToJobs);
		
		lbljobId = new JLabel("Select Job Id:");
		lbljobId.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbljobId.setBounds(10, 332, 134, 28);
		JobTab.add(lbljobId);
		
		JobIdBox = new JComboBox<Object>();
		JobIdBox.setFont(font1);
		JobIdBox.setBounds(23, 386, 51, 34);
		JobTab.add(JobIdBox);
		
		lblinterview = new JLabel("Comments");
		lblinterview.setBounds(220, 345, 197, 30);
		lblinterview.setFont(new Font("Arial", Font.ITALIC, 20));
		JobTab.add(lblinterview);
		
		InterviewPanel = new JTextPane();
		InterviewPanel.setFont(font1);
		InterviewPanel.setBounds(140, 373, 333, 123);
		JobTab.add(InterviewPanel);
		
	    btnApplayJob = new JButton("Apply");
		btnApplayJob.setBounds(215, 525, 139, 52);
		btnApplayJob.setFont(font1);
		JobTab.add(btnApplayJob);
		
		lblUpdateInterview = new JLabel("Update Application");
		lblUpdateInterview.setBounds(841, 319, 207, 34);
		lblUpdateInterview.setFont(new Font("Arial", Font.ITALIC, 24));
		JobTab.add(lblUpdateInterview);
		
		lblUpdateApplication = new JLabel("Select Job Id:");
		lblUpdateApplication.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUpdateApplication.setBounds(841, 363, 122, 28);
		JobTab.add(lblUpdateApplication);
		
		UpdateAppBox = new JComboBox<Object>();
		UpdateAppBox.setFont(font1);
		UpdateAppBox.setBounds(975, 357, 51, 34);
		JobTab.add(UpdateAppBox);
		
		lblNewInterview = new JLabel("Write New Comments");
		lblNewInterview.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewInterview.setBounds(841, 404, 185, 32);
		JobTab.add(lblNewInterview);
		
		NewInterview = new JTextPane();
		NewInterview.setFont(font1);
		NewInterview.setBounds(841, 436, 280, 100);
		JobTab.add(NewInterview);
		
		btnUpdateApp = new JButton("Update");
		btnUpdateApp.setBounds(1145, 468, 123, 50);
		btnUpdateApp.setFont(font1);
		JobTab.add(btnUpdateApp);
		ProfTab.setLayout(null);
		
		Languagelist = new JList<Object>();
		Languagelist.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		Languagelist.setBackground(Color.LIGHT_GRAY);
		Languagelist.setBounds(10, 367, 532, 51);
		ProfTab.add(Languagelist);
		
		rdbtnShowBio = new JRadioButton("Show Bio");
		rdbtnShowBio.setBounds(548, 219, 105, 21);
		ProfTab.add(rdbtnShowBio);
		
		JLabel label = new JLabel("Add Language");
		label.setFont(font1);
		label.setBounds(41, 431, 144, 29);
		ProfTab.add(label);
		
		String[] req = {"","EN","FR","SP","GR","EN,FR","EN,SP","EN,GR","SP,FR","FR,GR","EN,GR,SP","EN,GR,FR","FR,SP,GR","EN,FR,SP,GR"};
		LangcomboBox = new JComboBox(req);
		LangcomboBox.setFont(new Font("Arial", Font.PLAIN, 15));
		LangcomboBox.setBounds(41, 473, 102, 44);
		ProfTab.add(LangcomboBox);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setFont(font1);
		btnSubmit.setBounds(190, 526, 105, 41);
		ProfTab.add(btnSubmit);
		
		
		loadUserStatus();
		
		loadEmplStatus();
		
		loadDegree();
		
		loadLanguage();
		
		loadAvailJobs();
		
		loadAppliedJobs();
		
		fillJobCodesComboBox(); //Applied Jobs
		
		fillJobIdBox(); //Apply for Job
		
		loadProject();
		
		fillInstBox();
		
		fillTitleBox();
		
		
		
	}
			
			
	private void EmplEvents() {
	         
		    //For Profile Tab
			//show old password
			rdbtnShowOldPas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(rdbtnShowOldPas.isSelected()){
						
						OldPasField.setEchoChar((char)0);
					}
					else {
						OldPasField.setEchoChar('*');
					}
				}
			});
			
			//show password on list
			rdbtnShPasList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(rdbtnShPasList.isSelected()){    
        				JOptionPane.showMessageDialog(null,"Your Password is: "+psd);    
        				} 
				}
			});
			
			
			//Show Bio
			rdbtnShowBio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (rdbtnShowBio.isSelected()) {
						ShowBio();
					}
				}
			});
			
			
			//show new password
			rdbtnShowNewPas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (rdbtnShowNewPas.isSelected()) {
						
						NewPasField.setEchoChar((char)0);
					}
					else {
						NewPasField.setEchoChar('*');
					}
				}
			});
			
			
			// change password button
			btnChangePas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				if(String.valueOf(OldPasField.getPassword()).equals(String.valueOf(NewPasField.getPassword()))) {
					JOptionPane.showMessageDialog(null, "Cant use old password");
				}
				else {
					changeUserPas();
				}
				}
			});
			
			
			//change Bios
			UpBiosBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChangeBios();
				}
			});
			
			//For Jobs Tab
			//Delete Job Application
			btnDeleteApplication.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(DeleteJobBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null, "Select Job Code");
					}else
					{
						DeleteJobApplication();
					}
				}
			});
			
			
			//Apply for a Job
			btnApplayJob.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		if(JobIdBox.getSelectedIndex() == 0) {
		    			JOptionPane.showMessageDialog(null, "Select Job Code");
		    		}
		    		else {
		    		RequestEval();
		    		}
		    	}
		    });
			
			//Update Application
			btnUpdateApp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(UpdateAppBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null, "Select Job Code");
					}
					
					else {
						UpdateApplication();
					}
				}
			});
			
			// For More Tab
			//Add new Project
			btnAddProject.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(ProjectNameField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Add Project Name");
					}
					else if(ProjectUrlField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Add Project Username");
					}
					else {
						AddProject();
					}
				}
			});
			
			//Add Degree
			btnAddDegree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddDegree();
				}
			});
			
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddLang();
				}
			});
			
			titleComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				TitleField.setText(titleComboBox.getSelectedItem().toString());
				}
			});
			
			
			instComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				 InstituteField.setText(instComboBox.getSelectedItem().toString());
				}
			});
			
		
	}
	
}// end of empl class
