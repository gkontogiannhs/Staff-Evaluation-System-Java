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


public class AdminGUI extends JFrame {

	
	private static final long serialVersionUID = 1L;
	
    private Connection connection = null;
    
	private ResultSet rs = null;
	private CallableStatement cs = null;
	private PreparedStatement pst = null;
	
	private String username;
	
	JTableHeader header;
	
	private JPanel adminContent,userPanel,compPanel;
	private JTabbedPane tabbedPane;
	private GroupLayout gl_adminContent;
	private JComboBox userTypeComboBox;
	private JLabel typeLabel,usrLabel,psdLabel,nameLabel,surLabel,emailLabel,lblPleaseTypeUsers;
	private JTextField usrField,psdField,nameField,surField,mailField;
	private JPanel miscRegPanel;
	private JTextField compAfmField,expYearsField;
	private JLabel lblManagersevaluators,expYearsLabel,comp_afmLabel;
	private JPanel borderPanel;
	private JTextField refField,awardsField,cerField;
	private JLabel empLabel,cerLabel,awardsLabel, refLabel,bioLabel;
	private JTextField phoneField,streetField,numField,titleField,cityField,countryField,compNameField,doyField,afmField;
	private JLabel alreadyLabel;
	private JTable title_fieldTable;
	private JScrollPane tile_fieldScrollPane;
	private TextArea bioArea,descrArea;
	private JButton regUserBtn,regCompBtn;
	private JPanel regCompPanel,regTitle_FieldPanel;
	private JLabel numLabel,phoneLabel,streetLabel,cityLabel,countryLabel,afmLabel,doyLabel,compNameLabel,lblRegisterNewCompany,jobLabel,categoryLabel,lblRegisterNewJob,descrLabel;
	private JTextField belongsField;
	private JPanel activityPanel;
	private JTable activityTable;
	private JLabel seeByUserLabel;
	private JComboBox userComboBox,tableNameComboBox;
	private JLabel seeByTableLabel;
	private JScrollPane activityScrollPane;
	
	
	/*constructor for admin gui*/
	public AdminGUI() {
		makeAdminGUI();
		adminEvents();
	}
	
	
	//fills a combo box with all the job ids
	private void fillUsrComboBox() {
			
			try {
				 
				 String query = "SELECT username from user";
				 Statement st = connection.createStatement();
				 
				 rs = st.executeQuery(query);
				 userComboBox.addItem("");
				 while(rs.next()) {
				   userComboBox.addItem(rs.getString(1));
				 }
				 st.close();
				 rs.close();
				
				 
			 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }	
			
		}
		
		
	//loads log table either by table name as parameter either as user
	private void loadLogTableByTable()
	{
		try {
			String query = "SELECT num,user,date_time,action,kind FROM log WHERE table_name like ?";
			pst = connection.prepareStatement(query);
			pst.setString(1,tableNameComboBox.getSelectedItem().toString() );
			
			rs = pst.executeQuery();
			
			 activityTable.setModel(DbUtils.resultSetToTableModel(rs));
			 header = title_fieldTable.getTableHeader();
			 activityTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 19));
			 activityTable.setFont(new Font("Arial", Font.BOLD, 17));
		     header.setBackground(Color.gray);
			 
			 rs.close();
			 pst.close();
			
		}
		catch(Exception e){ JOptionPane.showMessageDialog(null, e);}
	}
	
	
	//loads log table either by table name as parameter either as user
	private void loadLogTableByUser()
		{
			try {
				 String query = "SELECT num,table_name,date_time,action,kind FROM log WHERE user = ?";
				 pst = connection.prepareStatement(query);
				 pst.setString(1,userComboBox.getSelectedItem().toString() );
				
				 rs = pst.executeQuery();
				
				 activityTable.setModel(DbUtils.resultSetToTableModel(rs));
				 header = title_fieldTable.getTableHeader();
				 activityTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 19));
				 activityTable.setFont(new Font("Arial", Font.BOLD, 17));
			     header.setBackground(Color.gray);
				 
				 rs.close();
				 pst.close();
				
			}
			catch(Exception e){ JOptionPane.showMessageDialog(null, e);}
		}
	
	
	//calls procedure and make a new user register
	private void registerCompany()
		{
			try {
					 String stored = "{call regNewCompany(?,?,?,?,?,?,?,?)}";
					 cs = connection.prepareCall(stored);
					 
					 cs.setString(1, afmField.getText());
					 cs.setString(2, doyField.getText());
					 cs.setString(3, compNameField.getText());
			         cs.setString(4, phoneField.getText());
			         cs.setString(5, streetField.getText());
			         cs.setString(6, numField.getText());
			         cs.setString(7, cityField.getText());
			         cs.setString(8, countryField.getText());
			         
			         cs.execute();
					 JOptionPane.showMessageDialog(null,"Succesfull company register.");
					 
					 cs.close();
					 
					} catch(SQLException e){
					    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
					    	JOptionPane.showMessageDialog(null,"This is already a register."); }
				
				      finally {
				    	 afmField.setText("");
						 doyField.setText("");
						 compNameField.setText("");
						 phoneField.setText("");
						 streetField.setText("");
						 numField.setText("");
						 cityField.setText("");
						 countryField.setText("");
						    }
		}
	
	
	//calls procedure and make a new user register
	private void registerField(){
			try {
				 String stored = "{call regNewField(?,?,?)}"; 
				 cs = connection.prepareCall(stored);
				 
				 cs.setString(1, titleField.getText());
				 cs.setString(2, descrArea.getText());
				 cs.setString(3, belongsField.getText());
				 
		         cs.execute();
				 JOptionPane.showMessageDialog(null,"Succesfull title/field register.");
				 
				 cs.close();
			       
				}   catch(SQLException e){
				    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
				    	JOptionPane.showMessageDialog(null,"This is already a register."); }
			
			      finally {
			    	 titleField.setText("");
					 descrArea.setText("");
					 belongsField.setText("");
					    }		
			}
		
		
			
	//calls procedure and make a new user register
	private void registerUser()
	{
		try {
				 String stored = "{call regNewUser(?,?,?,?,?)}";
				 cs = connection.prepareCall(stored);
				 
				 username = usrField.getText();//use this for the other register procedures
				 
				 cs.setString(1, username);
				 cs.setString(2, psdField.getText());
				 cs.setString(3, nameField.getText());
		         cs.setString(4, surField.getText());
		         cs.setString(5, mailField.getText());
		         
		         cs.execute();
				 JOptionPane.showMessageDialog(null,"Succesfull user register.");
				 
				 cs.close();
				 
				} catch(SQLException e){
				    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
				    	JOptionPane.showMessageDialog(null,"This is already a register."); }
			
			      finally {
			    	 usrField.setText("");
					 psdField.setText("");
					 nameField.setText("");
					 surField.setText("");
					 mailField.setText(""); 
					    }
	}
	
	
	//calls procedure and make a new employee register
	 private void registerEmployee()
		{
			try {
					 String stored = "{call regNewEmployee(?,?,?,?,?,?,?)}";
					 cs = connection.prepareCall(stored);
					 
					 cs.setString(1, username);
					 cs.setString(2, compAfmField.getText());
					 cs.setString(3, bioArea.getText() );
			         cs.setString(4, expYearsField.getText());
			         cs.setString(5, cerField.getText());
			         cs.setString(6, awardsField.getText());
			         cs.setString(7, refField.getText());
			       
					 cs.execute(); 
					 JOptionPane.showMessageDialog(null,"Succesfull employee register.");
					 
					 cs.close();
					 
					} catch(SQLException e){
					    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
					    	JOptionPane.showMessageDialog(null,"This is already a register."); }
				
				      finally {
				    	expYearsField.setText("");
						compAfmField.setText(""); 
						expYearsField.setText("");
						cerField.setText("");
						awardsField.setText("");
						refField.setText("");
						bioArea.setText("");
						    }
		}
		
		
	//registers new manager
	private void registerManager() {
		try {
			 String stored = "{call regNewManager(?,?,?)}";
			 CallableStatement cs = connection.prepareCall(stored);
				
			 cs.setString(1, username);
			 cs.setString(2, expYearsField.getText());
			 cs.setString(3, compAfmField.getText() );
	     
	       
			 cs.execute();
			 JOptionPane.showMessageDialog(null,"Succesfull manager register.");
			 
			 cs.close();
			 
			} catch(SQLException e){
			    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
			    	JOptionPane.showMessageDialog(null,"This is already a register."); }
		
		      finally {
		    	 expYearsField.setText("");
				 compAfmField.setText("");
				    }
	}

	
	//registers new evaluator
	private void registerEvaluator() {
		try {
			 String stored = "{call regNewEvaluator(?,?,?)}";
			 cs = connection.prepareCall(stored);
				
			 cs.setString(1, username);
			 cs.setString(2, expYearsField.getText());
			 cs.setString(3, compAfmField.getText());
	     
	       
			 cs.execute();
			 JOptionPane.showMessageDialog(null,"Succesfull evaluator register.");
			 
			 cs.close();
			 
			} catch(SQLException e){
			    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
			    	JOptionPane.showMessageDialog(null,"This is already a register."); }
		
		      finally {
		    	 expYearsField.setText("");
				 compAfmField.setText("");
				    }
	}
	
	
	//loads a table with job fields/categories 
    private void loadJobFieldTable() {

		try {
			 String query = "SELECT DISTINCT CONCAT(title,'-',belongs_to) AS 'Fields/Activities' FROM field";
			 pst = connection.prepareStatement(query);
			 rs = pst.executeQuery();
			 
			 title_fieldTable.setModel(DbUtils.resultSetToTableModel(rs));
			 header = title_fieldTable.getTableHeader();
			 title_fieldTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 19));
			 title_fieldTable.setFont(new Font("Arial", Font.BOLD, 17));
		     header.setBackground(Color.gray);
			 
			 rs.close();
			 pst.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }	
		
	}
	
    
	//gui
	private void makeAdminGUI() {
		
		connection = dbCon.connectToDB();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1442, 641);
		adminContent = new JPanel();
		adminContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		
		setContentPane(adminContent);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		gl_adminContent = new GroupLayout(adminContent);
		gl_adminContent.setHorizontalGroup(
			gl_adminContent.createParallelGroup(Alignment.LEADING)
			.addGroup(gl_adminContent.createSequentialGroup()
			.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 1409, GroupLayout.PREFERRED_SIZE)
			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		gl_adminContent.setVerticalGroup(
			gl_adminContent.createParallelGroup(Alignment.LEADING)
			.addGroup(gl_adminContent.createSequentialGroup()
			.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 582, GroupLayout.PREFERRED_SIZE)
		    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		
		//some fonts
		Font font1 = new Font("Tahoma", Font.PLAIN, 20);
		Font font2 = new Font("Tahoma", Font.PLAIN, 24);
		
		
		userPanel = new JPanel();
		tabbedPane.addTab("Create User", null, userPanel, null);
		userPanel.setLayout(null);
		
		String[] type = {"\n","Manager\t", "Evaluator", "Employee"};
		userTypeComboBox = new JComboBox(type);
		userTypeComboBox.setFont(font1);
		userTypeComboBox.setBounds(895, 14, 129, 36);
		userPanel.add(userTypeComboBox);
		
		typeLabel = new JLabel("Please Select Type of User:");
		typeLabel.setFont(font2);
		typeLabel.setBounds(526, 10, 312, 41);
		userPanel.add(typeLabel);
		
		usrLabel = new JLabel("Username:");
		usrLabel.setFont(font1);
		usrLabel.setBounds(27, 83, 106, 36);
		userPanel.add(usrLabel);
		
		psdLabel = new JLabel("Password:");
		psdLabel.setFont(font1);
		psdLabel.setBounds(27, 157, 98, 36);
		userPanel.add(psdLabel);
		
		nameLabel = new JLabel("Name:");
		nameLabel.setFont(font1);
		nameLabel.setBounds(27, 231, 76, 36);
		userPanel.add(nameLabel);
		
		surLabel = new JLabel("Surname:");
		surLabel.setFont(font1);
		surLabel.setBounds(27, 304, 98, 36);
		userPanel.add(surLabel);
		
		emailLabel = new JLabel("Email:");
		emailLabel.setFont(font1);
		emailLabel.setBounds(27, 375, 64, 36);
		userPanel.add(emailLabel);
		
		lblPleaseTypeUsers = new JLabel("Please type user's info:");
		lblPleaseTypeUsers.setFont(font2);
		lblPleaseTypeUsers.setBounds(12, 13, 261, 41);
		userPanel.add(lblPleaseTypeUsers);
		
		usrField = new JTextField();
		usrField.setFont(font1);
		usrField.setBounds(142, 83, 176, 36);
		userPanel.add(usrField);
		usrField.setColumns(10);
		
		psdField = new JTextField();
		psdField.setFont(font1);
		psdField.setColumns(10);
		psdField.setBounds(142, 160, 176, 36);
		userPanel.add(psdField);
		
		nameField = new JTextField();
		nameField.setFont(font1);
		nameField.setColumns(10);
		nameField.setBounds(142, 231, 176, 36);
		userPanel.add(nameField);
		
		surField = new JTextField();
		surField.setFont(font1);
		surField.setColumns(10);
		surField.setBounds(142, 304, 176, 36);
		userPanel.add(surField);
		
		mailField = new JTextField();
		mailField.setFont(font1);
		mailField.setColumns(10);
		mailField.setBounds(142, 375, 176, 36);
		userPanel.add(mailField);
		
		borderPanel = new JPanel();
		borderPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "User Specification:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		borderPanel.setBounds(430, 44, 947, 489);
		userPanel.add(borderPanel);
		borderPanel.setLayout(null);
		
		miscRegPanel = new JPanel();
		miscRegPanel.setBounds(12, 13, 935, 484);
		borderPanel.add(miscRegPanel);
		miscRegPanel.setLayout(null);

		compAfmField = new JTextField();
		compAfmField.setBounds(167, 129, 149, 36);
		compAfmField.setFont(font1);
		compAfmField.setColumns(10);
		miscRegPanel.add(compAfmField);
		
		expYearsField = new JTextField();
		expYearsField.setFont(font1);
		expYearsField.setColumns(10);
		expYearsField.setBounds(167, 79, 149, 36);
		miscRegPanel.add(expYearsField);
		
		lblManagersevaluators = new JLabel("Manager's/Evaluator's/Employee's:");
		lblManagersevaluators.setFont(font2);
		lblManagersevaluators.setBounds(12, 13, 377, 41);
		miscRegPanel.add(lblManagersevaluators);
		
		expYearsLabel = new JLabel("Years Of Exp. :");
		expYearsLabel.setFont(font1);
		expYearsLabel.setBounds(12, 79, 143, 36);
		miscRegPanel.add(expYearsLabel);
		
		comp_afmLabel = new JLabel("Company AFM:");
		comp_afmLabel.setFont(font1);
		comp_afmLabel.setBounds(12, 128, 143, 36);
		miscRegPanel.add(comp_afmLabel);
		
		refField = new JTextField();
		refField.setFont(font1);
		refField.setColumns(10);
		refField.setBounds(747, 214, 176, 36);
		miscRegPanel.add(refField);
		
		awardsField = new JTextField();
		awardsField.setFont(font1);
		awardsField.setColumns(10);
		awardsField.setBounds(747, 160, 176, 36);
		miscRegPanel.add(awardsField);
		
		cerField = new JTextField();
		cerField.setFont(font1);
		cerField.setColumns(10);
		cerField.setBounds(747, 101, 176, 36);
		miscRegPanel.add(cerField);
		
		empLabel = new JLabel("Employee's Status:");
		empLabel.setFont(font2);
		empLabel.setBounds(563, 13, 215, 41);
		miscRegPanel.add(empLabel);
		
		cerLabel = new JLabel("Certificates (pdf):");
		cerLabel.setFont(font1);
		cerLabel.setBounds(563, 100, 164, 36);
		miscRegPanel.add(cerLabel);
		
		awardsLabel = new JLabel("Awards (pdf):");
		awardsLabel.setFont(font1);
		awardsLabel.setBounds(563, 159, 143, 36);
		miscRegPanel.add(awardsLabel);
		
		refLabel = new JLabel("References (pdf):");
		refLabel.setFont(font1);
		refLabel.setBounds(563, 213, 164, 36);
		miscRegPanel.add(refLabel);
		
		bioLabel = new JLabel("Biography:");
		bioLabel.setFont(font1);
		bioLabel.setBounds(563, 262, 96, 36);
		miscRegPanel.add(bioLabel);
		
		bioArea = new TextArea();
		bioArea.setFont(font1);
		bioArea.setBounds(563, 304, 349, 170);
		miscRegPanel.add(bioArea);
		
		regUserBtn = new JButton("Finish");
		regUserBtn.setFont(font1);
		regUserBtn.setBounds(58, 365, 143, 54);
		miscRegPanel.add(regUserBtn);
		
		compPanel = new JPanel();
		tabbedPane.addTab("Create Company", null, compPanel, null);
		compPanel.setLayout(null);
		
		regCompPanel = new JPanel();
		regCompPanel.setLayout(null);
		regCompPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Company stats", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		regCompPanel.setBounds(12, 13, 460, 514);
		compPanel.add(regCompPanel);
		
		numLabel = new JLabel("St. number:");
		numLabel.setFont(font1);
		numLabel.setBounds(32, 323, 113, 29);
		regCompPanel.add(numLabel);
		
		phoneLabel = new JLabel("Phone num:");
		phoneLabel.setFont(font1);
		phoneLabel.setBounds(32, 239, 135, 29);
		regCompPanel.add(phoneLabel);
		
		streetLabel = new JLabel("Street:");
		streetLabel.setFont(font1);
		streetLabel.setBounds(32, 281, 146, 29);
		regCompPanel.add(streetLabel);
		
		cityLabel = new JLabel("City:");
		cityLabel.setFont(font1);
		cityLabel.setBounds(32, 365, 88, 29);
		regCompPanel.add(cityLabel);
		
		countryLabel = new JLabel("Country:");
		countryLabel.setFont(font1);
		countryLabel.setBounds(32, 407, 88, 29);
		regCompPanel.add(countryLabel);
		
		phoneField = new JTextField();
		phoneField.setFont(font1);
		phoneField.setColumns(10);
		phoneField.setBounds(183, 238, 178, 29);
		regCompPanel.add(phoneField);
		
		streetField = new JTextField();
		streetField.setFont(font1);
		streetField.setColumns(10);
		streetField.setBounds(183, 280, 178, 29);
		regCompPanel.add(streetField);
		
		numField = new JTextField();
		numField.setFont(font1);
		numField.setColumns(10);
		numField.setBounds(183, 322, 178, 29);
		regCompPanel.add(numField);
		
		cityField = new JTextField();
		cityField.setFont(font1);
		cityField.setColumns(10);
		cityField.setBounds(183, 364, 178, 29);
		regCompPanel.add(cityField);
		
		countryField = new JTextField();
		countryField.setFont(font1);
		countryField.setColumns(10);
		countryField.setBounds(183, 406, 178, 29);
		regCompPanel.add(countryField);
		
		compNameField = new JTextField();
		compNameField.setFont(font1);
		compNameField.setColumns(10);
		compNameField.setBounds(183, 196, 178, 29);
		regCompPanel.add(compNameField);
		
		doyField = new JTextField();
		doyField.setFont(font1);
		doyField.setColumns(10);
		doyField.setBounds(183, 154, 178, 29);
		regCompPanel.add(doyField);
		
		afmField = new JTextField();
		afmField.setFont(font1);
		afmField.setColumns(10);
		afmField.setBounds(183, 112, 178, 29);
		regCompPanel.add(afmField);
		
	    afmLabel = new JLabel("AFM:");
		afmLabel.setFont(font1);
		afmLabel.setBounds(32, 112, 135, 29);
		regCompPanel.add(afmLabel);
		
		doyLabel = new JLabel("DOY:");
		doyLabel.setFont(font1);
		doyLabel.setBounds(32, 154, 135, 29);
		regCompPanel.add(doyLabel);
		
		compNameLabel = new JLabel("Comp. Name:");
		compNameLabel.setFont(font1);
		compNameLabel.setBounds(32, 196, 135, 29);
		regCompPanel.add(compNameLabel);
		
		lblRegisterNewCompany = new JLabel("Register New Company:");
		lblRegisterNewCompany.setBounds(32, 40, 278, 29);
		regCompPanel.add(lblRegisterNewCompany);
		lblRegisterNewCompany.setFont(font2);
		lblRegisterNewCompany.setBackground(Color.WHITE);
		
		regTitle_FieldPanel = new JPanel();
		regTitle_FieldPanel.setBorder(new TitledBorder(null, "Company Job Fields", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		regTitle_FieldPanel.setBounds(500, 24, 880, 380);
		compPanel.add(regTitle_FieldPanel);
		regTitle_FieldPanel.setLayout(null);
		
		titleField = new JTextField();
		titleField.setBounds(147, 161, 178, 29);
		regTitle_FieldPanel.add(titleField);
		titleField.setFont(font1);
		titleField.setColumns(10);
		
		jobLabel = new JLabel("Job Title:");
		jobLabel.setBounds(12, 162, 104, 29);
		regTitle_FieldPanel.add(jobLabel);
		jobLabel.setFont(font1);
		
		categoryLabel = new JLabel("Job Category:");
		categoryLabel.setBounds(12, 97, 135, 40);
		regTitle_FieldPanel.add(categoryLabel);
		categoryLabel.setFont(font1);
		
		lblRegisterNewJob = new JLabel("Register New Job Fields: (Non Mandatory)");
		lblRegisterNewJob.setFont(font2);
		lblRegisterNewJob.setBackground(Color.WHITE);
		lblRegisterNewJob.setBounds(12, 32, 459, 29);
		regTitle_FieldPanel.add(lblRegisterNewJob);
		
		alreadyLabel = new JLabel("Already Registered:");
		alreadyLabel.setFont(font1);
		alreadyLabel.setBackground(Color.WHITE);
		alreadyLabel.setBounds(579, 32, 213, 29);
		regTitle_FieldPanel.add(alreadyLabel);
		
		tile_fieldScrollPane = new JScrollPane();
		tile_fieldScrollPane.setBounds(522, 85, 332, 137);
		regTitle_FieldPanel.add(tile_fieldScrollPane);
		
		title_fieldTable = new JTable();
		tile_fieldScrollPane.setViewportView(title_fieldTable);
		
		descrArea = new TextArea();
		descrArea.setFont(font1);
		descrArea.setBounds(28, 255, 297, 115);
		regTitle_FieldPanel.add(descrArea);
		
		descrLabel = new JLabel("Job Description:");
		descrLabel.setFont(font1);
		descrLabel.setBounds(12, 220, 178, 29);
		regTitle_FieldPanel.add(descrLabel);
		
		belongsField = new JTextField();
		belongsField.setFont(font1);
		belongsField.setColumns(10);
		belongsField.setBounds(147, 109, 178, 29);
		regTitle_FieldPanel.add(belongsField);
		
		regCompBtn = new JButton("Finish");
		regCompBtn.setBounds(863, 463, 166, 46);
		compPanel.add(regCompBtn);
		regCompBtn.setFont(font1);
		
		activityPanel = new JPanel();
		tabbedPane.addTab("See Companies Activities", null, activityPanel, null);
		activityPanel.setLayout(null);
		
		activityScrollPane = new JScrollPane();
		activityScrollPane.setBounds(12, 85, 1380, 402);
		activityPanel.add(activityScrollPane);
		
		activityTable = new JTable();
		activityScrollPane.setViewportView(activityTable);
		
		String[] tables = {"Request_Evaluation", "Employee", "Job"};
		tableNameComboBox = new JComboBox(tables);
		tableNameComboBox.setFont(font1);
		tableNameComboBox.setBounds(1050, 27, 212, 35);
		activityPanel.add(tableNameComboBox);
		
		seeByUserLabel = new JLabel("See log Table by User:");
		seeByUserLabel.setFont(font1);
		seeByUserLabel.setBounds(23, 24, 231, 30);
		activityPanel.add(seeByUserLabel);
		
		userComboBox = new JComboBox();
		userComboBox.setFont(font1);
		userComboBox.setBounds(283, 27, 158, 35);
		activityPanel.add(userComboBox);
		
		seeByTableLabel = new JLabel("See log Table by Table Name:");
		seeByTableLabel.setFont(font1);
		seeByTableLabel.setBounds(705, 24, 290, 30);
		activityPanel.add(seeByTableLabel);
		adminContent.setLayout(gl_adminContent);
		
		loadJobFieldTable();
		
		loadLogTableByTable();
		
		fillUsrComboBox();
	}
    
	
	
	//this methos holds all admin's events
	private void adminEvents() {
		
		regCompBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			 
				 registerCompany();
				 if(!titleField.getText().isEmpty() || !belongsField.getText().isEmpty()) {
					 registerField();
					 loadJobFieldTable();  }
			}
		});
		
		regUserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				 if (userTypeComboBox.getSelectedIndex() == 0) {JOptionPane.showMessageDialog(null,"Invalid Option. Please choose User Type.");}
				 else registerUser();
				 
                 if ( userTypeComboBox.getSelectedIndex() == 1 )  { registerManager();  }
				 
				 else if (userTypeComboBox.getSelectedIndex() == 2)  { registerEvaluator();}
				 
                 else if (userTypeComboBox.getSelectedIndex() == 3)  { registerEmployee(); }
			}
		});
		
		tableNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadLogTableByTable();
			}
		});
		
		userComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadLogTableByUser();
			}
		});
		
	}
}//end of class AdminGUI
