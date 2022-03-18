package DB_GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.proteanit.sql.DbUtils;
import java.sql.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.awt.event.ActionEvent;


public class ManagerGUI extends JFrame {

	
	private static final long serialVersionUID = 1L;
	
	//super important
    private static String usr = guiForDB.getUsername();
    
    //connection object
    private Connection connection = null;
    
    private String psd;
    
    private ResultSet rs = null;
	private CallableStatement cs = null;
	private PreparedStatement pst = null;
	
	private JTableHeader header;
	
	/*profTab-->compPanel,settingsPanel instances */
	private JPanel initPanel,Tab, profTab,compPanel,settingsPanel;
	private JPasswordField pasField;
	private JTextField emailField, phoneField, streetField ,numberField, cityField, countryField;
	private JList<Object> userList,compList;
	private JLabel chgLabel,pasLabel,emailLabel,phoneLabel,streetLabel,numLabel,cityLabel,countryLabel;
	private JButton finishBtn;
	private JCheckBox PasCheckBox;
	private JRadioButton pasRadioBtn;
	private JTabbedPane manerTabbedPane;
	private GroupLayout gl_initPanel;
	/*end of proTab instances */
	
	
	/*Tab-> .... instances */
	private JTable mngrTable;
	private JScrollPane scrollPane,scrollPaneEmpl,skillsScrollPane;
	private JTextField chgSalField;
	private JTextField gradeField;
	private JComboBox mngerOptCb,jobComboBox,usrComboBox,empUsrComboBox ;
	private JLabel jobSalLabel,gradeLabel;
	private JButton mngrBtn,updBtn;
	private JTable employeeTable,skillsTable;
	private JTextField certField, awardField, refField;
	private JLabel lblCertificates,lblAwards,lblReferences,lblNewLabel,selectLabel,pdfLabel;
	/*end of Tab instances */
	

	
	
       /*call of constructor */
		public ManagerGUI() {
			createManagerGui();
			managerEvents();
		}
		
		
		//loads employee stuff
		private void loadEmplTable() {
			try {
				 String stored = "{call  returnEmp(?,?)}";
				 cs = connection.prepareCall(stored);

				 cs.setString(1,usr);
				 cs.setString(2, empUsrComboBox.getSelectedItem().toString());
			
				 rs = cs.executeQuery();
				 employeeTable.setModel(DbUtils.resultSetToTableModel(rs));
				 header = employeeTable.getTableHeader();
				 employeeTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
			     header.setBackground(Color.gray);
			     employeeTable.setFont(new Font("Arial",Font.ITALIC,14));
				 
				 rs.close();
				 cs.close();
				 
			 }
			 catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		}
		
		
		//loads employee skills
		private void loadEmplSkillsTable() {
		  try {
			String stored = "{call  emplProjDegLan(?)}";
			cs = connection.prepareCall(stored);

			cs.setString(1, empUsrComboBox.getSelectedItem().toString());
					
		    rs = cs.executeQuery();
		    
	   	    skillsTable.setModel(DbUtils.resultSetToTableModel(rs));
	   	    
		    header = skillsTable.getTableHeader();
			skillsTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
			header.setBackground(Color.gray);
			skillsTable.setFont(new Font("Arial",Font.ITALIC,14));
			
						 
			rs.close();
			cs.close();
						 
					 }
			catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
				}
		
		
		
		//method which returns the avg evaluation grade for every evaluator
		private void avgEval() {
			try {
				 
				 String stored = "{call avgEv(?)}";
				 cs = connection.prepareCall(stored);
	
				 cs.setString(1,usr);
                 rs = cs.executeQuery();

				  mngrTable.setModel(DbUtils.resultSetToTableModel(rs));
				  header = mngrTable.getTableHeader();
				  mngrTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
			      header.setBackground(Color.gray);
				 
				 rs.close();
				 cs.close();
				 
			 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		}
		
		
		
		//loads the user profile data for specific user
		private void loadUserStatus() {
			try {
				String query = "SELECT * FROM user WHERE username = ? ";
				pst = connection.prepareStatement(query);
				pst.setString(1, usr);
				rs = pst.executeQuery();
				
	            DefaultListModel<Object> dlm = new DefaultListModel<Object>();
				
	            while(rs.next()) {
				dlm.addElement("Username:  "+rs.getString("username")); // can also use usr variable
			    psd = rs.getString("password");
				dlm.addElement("Password:  Press button to see password.");
				dlm.addElement("Name:  "+rs.getString("name"));
				dlm.addElement("Surname:  "+rs.getString("surname"));
				dlm.addElement("Register Date:  "+rs.getString("register_date"));
				dlm.addElement("Email:  "+rs.getString("email"));
	                            }
	            
				userList.setModel(dlm);
				
				pst.close();
				rs.close();
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
		
		//updates manager's password or email
		private void updateUserInfo() {
			
			 try {
				 
				 String query = "UPDATE user SET password = ? ,  email = ? WHERE username = ? ";
				 pst = connection.prepareStatement(query);
				 
				 pst.setString(1, String.valueOf(pasField.getPassword()));
				 pst.setString(2, emailField.getText());
				 pst.setString(3, usr);
				 
				 pst.execute();
				 
				 JOptionPane.showMessageDialog(null,"Succesfull user info updapte.");
				 pasField.setText("");
				 emailField.setText("");
				 
				 
				 pst.close();
				 //connection.close();
				 
			 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
			 loadUserStatus();
			}
		
		
		
		//updates company's stats
		private void updateCompInfo() {
			
			 try {
				 
				 String query = "{call updateCompany(?,?,?,?,?,?)}";
				 cs = connection.prepareCall(query);
	
				 cs.setString(1, usr);
				 
				if(!phoneField.getText().isEmpty()) {
					
				 BigInteger bigIntegerStr=new BigInteger(phoneField.getText());
				 cs.setObject(2,bigIntegerStr); }
				 else cs.setObject(2, 0);
				 
				 cs.setString(3, streetField.getText());
				 
				 if(!numberField.getText().isEmpty()) { cs.setObject(4,numberField.getText()); }
				 else cs.setObject(4, 0);
				 
				 //cs.setInt(4,Integer.valueOf(numberField.getText()));
				 cs.setString(5, cityField.getText());
				 cs.setString(6, countryField.getText());
				 
				 cs.execute();
				 
				 JOptionPane.showMessageDialog(null,"Succesfull company info updapte.");
				 phoneField.setText("");
				 streetField.setText("");
				 numberField.setText("");
				 cityField.setText("");
				 countryField.setText("");
				 
				 
				 cs.close();
				 //connection.close();
				 
			 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
			 loadCompStatus();
			}
		
		
		// loads company status
		private void loadCompStatus() {
			try {
				String query = "select * from company join manager on firm = afm where man_username = ?;";
				pst = connection.prepareStatement(query);
				pst.setString(1, usr);
				rs = pst.executeQuery();
				
				DefaultListModel<Object> dlm = new DefaultListModel<Object>();
				
	            while(rs.next())
	            {
				dlm.addElement("AFM:  "+rs.getString("afm")); // can also use usr variable
				dlm.addElement("DOY:  "+rs.getString("DOY"));
				dlm.addElement("Name:  "+rs.getString("comp_name"));
				dlm.addElement("Phone:  "+rs.getString("phone"));
				dlm.addElement("Street:  "+rs.getString("street"));
				dlm.addElement("St. Number:  "+rs.getString("num"));
				dlm.addElement("City:  "+rs.getString("city"));
				dlm.addElement("Country:  "+rs.getString("country"));
	             }
	            
				compList.setModel(dlm);
				
				pst.close();
				rs.close();
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
		
		
		//fill company job codes
		private void fillCompJobCodes() {
			try {
				
				 String query = "SELECT DISTINCT code,pos_title,salary FROM job j join evaluator e ON j.evaluator = e.ev_username join manager m ON m.firm = e.firm where man_username = ?";
				 pst = connection.prepareStatement(query);
				 pst.setString(1, usr);
				 
				 rs = pst.executeQuery();
				 
				 while(rs.next())
				 {
					 jobComboBox.addItem(rs.getString("code"));
				 }
			}
			    	catch(Exception e) {JOptionPane.showConfirmDialog(null, e);}  
			}
		
		
		//loads company's jobs
		private void loadCompJobs() {
			try {
				
				 String query = "SELECT DISTINCT code,pos_title,salary FROM job j join evaluator e ON j.evaluator = e.ev_username join manager m ON m.firm = e.firm where man_username = ?";
				 pst = connection.prepareStatement(query);
				 pst.setString(1, usr);
				 
				 rs = pst.executeQuery();
				 
				  mngrTable.setModel(DbUtils.resultSetToTableModel(rs));
				  
				  header = mngrTable.getTableHeader();
				  
				  mngrTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
			      header.setBackground(Color.gray);
			      mngrTable.setFont(new Font("Arial",Font.ITALIC,14));
				 
				 rs.close();
				 pst.close();
				
			}
			catch(Exception ex) {ex.printStackTrace();}
		}

		
		//method which fills usrComboBox usernames with no grade from manager
		private void fillUsrNoGrade() {
			try {
				 String query = "SELECT DISTINCT e_username FROM evaluation_info ei join evaluator e ON ei.evaluator = e.ev_username join manager m ON m.firm = e.firm where registered = 0 AND man_username = ?";
				 pst = connection.prepareStatement(query);
				 
				 pst.setString(1, usr);
				 
				 rs = pst.executeQuery();
				 
				 while(rs.next()) {
				  usrComboBox.addItem(rs.getString("e_username"));
				 }
				 pst.close();
				 
			 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }	
		}
		
		
		//updates company's future job salary
		private void updateSal(){
			try {
				 
				 String stored = "{call changeSalary(?,?)}";
				 cs = connection.prepareCall(stored);
	
				 cs.setInt(1,Integer.parseInt(jobComboBox.getSelectedItem().toString()));
				 cs.setFloat(2, Float.parseFloat(chgSalField.getText()));
				 
				 cs.execute();
				 
				 chgSalField.setText("");
				 
				 cs.close();
				 
			 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
			 loadCompStatus();
			}
		
		
		//methos which shows finalized company job requests
		private void compFinalized() {
			try {
			 String stored = "{call  ManShowEvRes(?)}";
			 cs = connection.prepareCall(stored);

			 cs.setString(1,usr);
		
			 rs = cs.executeQuery();
			 
			 mngrTable.setModel(DbUtils.resultSetToTableModel(rs));
			 
			 header = mngrTable.getTableHeader();
			 mngrTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		     header.setBackground(Color.gray);
			 
		     skillsTable.setFont(new Font("Arial",Font.ITALIC,14));
		     
			 rs.close();
			 cs.close();
			 
		 }
		 catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		}
		
		
		
		//method which fills combobox with empoloyees
		private void fillEmployees(){
			try {
				 String stored = "{call  returnEmpUsr(?)}";
				 cs = connection.prepareCall(stored);

				 cs.setString(1,usr);
			
				 rs = cs.executeQuery();
				 
				 while(rs.next()) {
					empUsrComboBox.addItem(rs.getString("username"));
				 }
				 
				 rs.close();
				 cs.close();
				 
			 }
			 catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		}
		
		
		
		//method which shows unfinalized company job requests
		private void compUnfinalized() {
			try {
				 String stored = "{call  ManShowNoGrade(?)}";
				 cs = connection.prepareCall(stored);

				 cs.setString(1,usr);
			
				  rs = cs.executeQuery();
				  mngrTable.setModel(DbUtils.resultSetToTableModel(rs));
				  header = mngrTable.getTableHeader();
				  mngrTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
			      header.setBackground(Color.gray);
			      mngrTable.setFont(new Font("Arial",Font.ITALIC,14));
				 
				 rs.close();
				 cs.close();
				 
			 }
			 catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
				}
				
				
		//updates employes certificates,awards,references by manager
		private void updEmpFolder() {
		 try {
			 String stored = "{call  updateEmp(?,?,?,?,?)}";
			 cs = connection.prepareCall(stored);

			 cs.setString(1,usr);
		     cs.setString(2, empUsrComboBox.getSelectedItem().toString());
		     cs.setString(3,certField.getText());
		     cs.setString(4, awardField.getText());
		     cs.setString(5, refField.getText());

		     cs.execute();
			 cs.close();
			 
			 JOptionPane.showMessageDialog(null, "Successful Folder Update." );
			 
		 }
		 catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
		 finally {
			 certField.setText("");
		     awardField.setText("");
		     refField.setText("");
		     loadEmplTable();
		}
	}
		
		
		//method which inserts a grade into evaluation_info table
		private void gradeEmployee() {
			try {
					
				String stored = "{call MangradeEmployee(?,?,?)}";
				cs = connection.prepareCall(stored);
				
				cs.setString(1, usrComboBox.getSelectedItem().toString());
				cs.setInt(2, Integer.parseInt(jobComboBox.getSelectedItem().toString()));
				cs.setInt(3,Integer.parseInt(gradeField.getText()));
				
				cs.execute();
			    
				JOptionPane.showMessageDialog(null,"Succesfull grading.");
				cs.close();
				
			}
				
			catch(SQLException e){
			    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
			    	JOptionPane.showMessageDialog(null,"This is already a register.");
			      }
			finally {   gradeField.setText("");
	                    compUnfinalized();
			         }
		}
		
		
		//method which constructs Manager frame
		private void createManagerGui(){
			
			connection = dbCon.connectToDB();
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 1433, 626);
			initPanel = new JPanel();
			initPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(initPanel);
			
			 manerTabbedPane = new JTabbedPane(JTabbedPane.TOP);
			 
			 gl_initPanel = new GroupLayout(initPanel);
			 gl_initPanel.setHorizontalGroup(
			 gl_initPanel.createParallelGroup(Alignment.LEADING)
			  .addGroup(gl_initPanel.createSequentialGroup()
			  .addComponent(manerTabbedPane, GroupLayout.PREFERRED_SIZE, 1403, GroupLayout.PREFERRED_SIZE)
			  .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			 
			gl_initPanel.setVerticalGroup(
				  gl_initPanel.createParallelGroup(Alignment.LEADING)
				   .addGroup(gl_initPanel.createSequentialGroup()
				   .addComponent(manerTabbedPane, GroupLayout.PREFERRED_SIZE, 570, GroupLayout.PREFERRED_SIZE)
				   .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			
			
			
			/* some basic fonts for usual use*/
            Font font1 = new Font("Arial",Font.ITALIC,20);
            Font font2 = new Font("Arial",Font.ITALIC,24);
            
            
            
            
            profTab = new JPanel();
            manerTabbedPane.addTab("Profile", null, profTab, null);
            profTab.setLayout(null);
            
            compPanel = new JPanel();
            compPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Company/Manager Profile Status", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            compPanel.setBounds(12, 13, 669, 514);
            profTab.add(compPanel);
            compPanel.setLayout(null);
            
            
            settingsPanel = new JPanel();
            settingsPanel.setLayout(null);
            settingsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Profile Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            settingsPanel.setBounds(721, 13, 630, 514);
            profTab.add(settingsPanel);
            
            
            ////////////// Lists for profile status
            userList = new JList<Object>();
            userList.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
            userList.setBackground(Color.LIGHT_GRAY);
            userList.setBounds(12, 51, 454, 163);
            compPanel.add(userList);
            
            compList = new JList<Object>();
            compList.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
            compList.setBackground(Color.LIGHT_GRAY);
            compList.setBounds(12, 269, 454, 208);
            compPanel.add(compList);
            
            
            //////////////////////////////////////////
            //Profile labels
            chgLabel = new JLabel("Change");
            chgLabel.setFont(font2);
            chgLabel.setBackground(Color.WHITE);
            chgLabel.setBounds(22, 34, 131, 29);
            settingsPanel.add(chgLabel);
            
            pasLabel = new JLabel("Password to:");
            pasLabel.setFont(font1);
            pasLabel.setBounds(32, 76, 135, 40);
            settingsPanel.add(pasLabel);
            
            emailLabel = new JLabel("Email to:");
            emailLabel.setFont(font1);
            emailLabel.setBounds(32, 126, 104, 29);
            settingsPanel.add(emailLabel);
            
            numLabel = new JLabel("St. number:");
            numLabel.setFont(font1);
            numLabel.setBounds(32, 323, 113, 29);
            settingsPanel.add(numLabel);
            
            phoneLabel = new JLabel("Phone num:");
            phoneLabel.setFont(font1);
            phoneLabel.setBounds(32, 239, 135, 29);
            settingsPanel.add(phoneLabel);
            
            streetLabel = new JLabel("Street:");
            streetLabel.setFont(font1);
            streetLabel.setBounds(32, 281, 146, 29);
            settingsPanel.add(streetLabel);
            
            
            cityLabel = new JLabel("City:");
            cityLabel.setFont(font1);
            cityLabel.setBounds(32, 365, 88, 29);
            settingsPanel.add(cityLabel);
            
            countryLabel = new JLabel("Country:");
            countryLabel.setFont(font1);
            countryLabel.setBounds(32, 407, 88, 29);
            settingsPanel.add(countryLabel);
            
            
            
            //////////////////////////////////////////////////////////////////// 
            // Text labels for profTab
            phoneField = new JTextField();
            phoneField.setFont(font1);
            phoneField.setColumns(10);
            phoneField.setBounds(183, 238, 178, 29);
            settingsPanel.add(phoneField);
            
            pasField = new JPasswordField();
            pasField.setFont(font1);
            pasField.setBounds(183, 81, 178, 29);
            settingsPanel.add(pasField);
            
            
            streetField = new JTextField();
            streetField.setFont(font1);
            streetField.setColumns(10);
            streetField.setBounds(183, 280, 178, 29);
            settingsPanel.add(streetField);
            
            numberField = new JTextField();
            numberField.setFont(font1);
            numberField.setColumns(10);
            numberField.setBounds(183, 322, 178, 29);
            settingsPanel.add(numberField);
            
            emailField = new JTextField();
            emailField.setFont(font1);
            emailField.setColumns(10);
            emailField.setBounds(183, 123, 178, 29);
            settingsPanel.add(emailField);
            
            cityField = new JTextField();
            cityField.setFont(font1);
            cityField.setColumns(10);
            cityField.setBounds(183, 364, 178, 29);
            settingsPanel.add(cityField);
            
			countryField = new JTextField();
			countryField.setFont(font1);
			countryField.setColumns(10);
			countryField.setBounds(183, 406, 178, 29);
			settingsPanel.add(countryField);
			
			//// misc buttons etc
			finishBtn = new JButton("Finish");
			finishBtn.setFont(font1);
			finishBtn.setBounds(143, 467, 151, 34);
			settingsPanel.add(finishBtn);
			
			pasRadioBtn = new JRadioButton("Show Password");
			pasRadioBtn.setFont(new Font("Arial", Font.ITALIC, 15));
            pasRadioBtn.setBounds(499, 88, 162, 25);
            compPanel.add(pasRadioBtn);
			
			PasCheckBox = new JCheckBox("Reveal");
			PasCheckBox.setBounds(369, 83, 113, 25);
			settingsPanel.add(PasCheckBox);
            // Panels for profile Panel :P

            Tab = new JPanel();
            manerTabbedPane.addTab("Manager Properties", null, Tab, null);
            Tab.setLayout(null);
            
            scrollPane = new JScrollPane();
            scrollPane.setBounds(23, 72, 614, 181);
            Tab.add(scrollPane);
            
            mngrTable = new JTable();
            scrollPane.setViewportView(mngrTable);
            
            String[] s = {"Company Jobs", "Unfinalized Job Requests", "Finalized Job Requests", "Evaluator Stats"};
            mngerOptCb = new JComboBox(s);
            mngerOptCb.setFont(font1);
            mngerOptCb.setBounds(260, 14, 270, 36);
            Tab.add(mngerOptCb);
            
            jobSalLabel = new JLabel("Change Salary:");
            jobSalLabel.setFont(font1);
            jobSalLabel.setBounds(23, 292, 147, 24);
            Tab.add(jobSalLabel);
            
            chgSalField = new JTextField();
            chgSalField.setFont(font1);
            chgSalField.setBounds(350, 290, 113, 28);
            Tab.add(chgSalField);
            chgSalField.setColumns(10);
            
            jobComboBox = new JComboBox();
            jobComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
            jobComboBox.setBounds(189, 286, 68, 35);
            Tab.add(jobComboBox);
            
            gradeLabel = new JLabel("Give Grade:");
            gradeLabel.setFont(font1);
            gradeLabel.setBounds(23, 340, 147, 24);
            Tab.add(gradeLabel);
            
            usrComboBox = new JComboBox();
            usrComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
            usrComboBox.setBounds(189, 334, 126, 36);
            Tab.add(usrComboBox);
            
            gradeField = new JTextField();
            gradeField.setFont(font1);
            gradeField.setColumns(10);
            gradeField.setBounds(350, 338, 113, 28);
            Tab.add(gradeField);
            
            mngrBtn = new JButton("Done");
            mngrBtn.setFont(font1);
            mngrBtn.setBounds(206, 444, 147, 49);
            Tab.add(mngrBtn);
            
            scrollPaneEmpl = new JScrollPane();
            scrollPaneEmpl.setBounds(696, 72, 690, 56);
            Tab.add(scrollPaneEmpl);
            
            employeeTable = new JTable();
            scrollPaneEmpl.setViewportView(employeeTable);
            
            updBtn = new JButton("Update Folder");
            updBtn.setFont(new Font("Arial", Font.PLAIN, 17));
            updBtn.setBounds(820, 478, 159, 49);
            Tab.add(updBtn);
            
            empUsrComboBox = new JComboBox();
            empUsrComboBox.setFont(font1);
            empUsrComboBox.setBounds(1038, 13, 159, 36);
            Tab.add(empUsrComboBox);
            
            certField = new JTextField();
            certField.setFont(font1);
            certField.setBounds(863, 338, 147, 28);
            Tab.add(certField);
            certField.setColumns(10);
            
            pdfLabel = new JLabel("Upload PDF file for:");
            pdfLabel.setFont(new Font("Arial", Font.PLAIN, 22));
            pdfLabel.setBounds(696, 288, 196, 28);
            Tab.add(pdfLabel);
            
            awardField = new JTextField();
            awardField.setFont(font1);
            awardField.setBounds(863, 379, 147, 28);
            Tab.add(awardField);
            awardField.setColumns(10);
            
            refField = new JTextField();
            refField.setFont(font1);
            refField.setBounds(863, 420, 147, 29);
            Tab.add(refField);
            refField.setColumns(10);
            
            lblCertificates = new JLabel("Certificates:");
            lblCertificates.setFont(font1);
            lblCertificates.setBounds(738, 337, 113, 28);
            Tab.add(lblCertificates);
            
            lblAwards = new JLabel("Awards:");
            lblAwards.setFont(font1);
            lblAwards.setBounds(737, 379, 97, 28);
            Tab.add(lblAwards);
            
            lblReferences = new JLabel("References:");
            lblReferences.setFont(font1);
            lblReferences.setBounds(738, 421, 113, 28);
            Tab.add(lblReferences);
            
            lblNewLabel = new JLabel("Select Option:");
            lblNewLabel.setFont(font1);
            lblNewLabel.setBounds(53, 13, 147, 36);
            Tab.add(lblNewLabel);
            
            selectLabel = new JLabel("Select Employee Folder:");
            selectLabel.setFont(font1);
            selectLabel.setBounds(733, 17, 262, 28);
            Tab.add(selectLabel);
            
            skillsScrollPane = new JScrollPane();
            skillsScrollPane.setBounds(696, 127, 690, 126);
            Tab.add(skillsScrollPane);
            
            skillsTable = new JTable();
            skillsScrollPane.setViewportView(skillsTable);
            initPanel.setLayout(gl_initPanel);
		
			//* call of methods */
            loadUserStatus();
            
            loadCompStatus();
            
            loadCompJobs();
            
            fillUsrNoGrade();
            
            fillCompJobCodes();
            
            fillEmployees();
            
            loadEmplTable();
            
            loadEmplSkillsTable();
		}
		
		
		//method which holds all the manager evetnts
		private void managerEvents() {
			
			finishBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if( (String.valueOf(pasField.getPassword()).isEmpty()) && (emailField.getText().isEmpty()) ) {
						updateCompInfo();
					}
					else if ( !(String.valueOf(pasField.getPassword()).isEmpty()) || !(emailField.getText().isEmpty()) ){
						updateUserInfo();
					}
					else JOptionPane.showMessageDialog(null, "Invalid Option.");
				}
			});
			
			
			PasCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (PasCheckBox.isSelected()) {
					      pasField.setEchoChar((char)0); 
					   } else {
					      pasField.setEchoChar('*');
					   }
				}
			});
			
			
            pasRadioBtn.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		if(pasRadioBtn.isSelected()){    
        				JOptionPane.showMessageDialog(null,"Your Password is: "+psd);    
        				}    
            	}
            });
            
            mngerOptCb.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		if(mngerOptCb.getSelectedIndex() == 0)  { loadCompJobs(); }
            		
            		else if(mngerOptCb.getSelectedIndex() == 1) { compUnfinalized();	}
            		
            		else if(mngerOptCb.getSelectedIndex() == 2)
            		 { compFinalized();	}
            		
            		else if(mngerOptCb.getSelectedIndex() == 3) {avgEval() ;}
            	}
            });
            
            mngrBtn.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		
            		if(mngerOptCb.getSelectedIndex() == 0) {
            			if(!chgSalField.getText().isEmpty()) {
            			updateSal();
            			JOptionPane.showMessageDialog(null, "Salary was updated succesfully.");
                        loadCompJobs(); }
            			else JOptionPane.showMessageDialog(null, "Invalid Option. Please give grade.");
            		}
            		else if (mngerOptCb.getSelectedIndex() == 1) {
            			gradeEmployee();
            			JOptionPane.showMessageDialog(null, "Grade was registered succesfully.");}
            		
            	}
            });
            
            empUsrComboBox.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		loadEmplTable();
            		loadEmplSkillsTable();
            	}
            });
            
            updBtn.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		updEmpFolder();
            	}
            });
			
		}
}//end of class
