package DB_GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.proteanit.sql.DbUtils;
import java.sql.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.LayoutStyle.ComponentPlacement;




public class EvaluatorGUI extends JFrame {

	
	private static final long serialVersionUID = 1L;
	
	
	//super important
    private static String usr = guiForDB.getUsername();
    
    private static String psd = guiForDB.getPassword();
    
    //connection object
    private Connection connection = null;
    
	private ResultSet rs = null;
	private CallableStatement cs = null;
	private PreparedStatement pst = null;
	
	
	
    // A heck of variables declaration mess
	private JPanel contentPane,profPanel , jobsPanel , evalPanel, settingsPanel ,statusPanel;
	private JButton regJobButton, btnFinish;
	private JLabel startdataLabel, salaryLabel, postitleLabel ,edraLabel,deadlineLabel;
	private JTextField startdateField, salaryField, jobTitleField, edraField, deadlineField;
	private JTable requestTable;
	private JPasswordField chgPsdField;
	private JScrollPane scrollPane;
	private JTextField chgNameField,chgSurField, chgDateField,chgEmailField;
	private JLabel chgEmailLabel,changePsdLabel,chgNameLabel,chgSurLabel,chgDateLabel,change;
	private JList<Object> list;
	private JCheckBox showPsd;
	private JRadioButton showPsdBtn;
	private JTable jobTable;
	private JScrollPane scrollJob;
	private JComboBox<Object> jobSelection;
	private JTabbedPane tabbedPane;
	private JPanel registerPanel;
	private JLabel fieldTitleLabel,evalGradeLabel,comLabel,lblRegisterNew,regTitleLbl,regBelLbl,regDescrLbl;
	private JTextField titleField,descrField,gradeField;
	private JButton updateBtn,gradeBtn,finalizeBtn,finishRegButton,logOutBtn;
	private JComboBox<String> jobCodesComboBox,fieldComboBox,employeeComboBox,codeComboBox,resultComboBox,resultsJobCodeComboBox,jobCodesNeedsCb,requestComboBox;
	private JPanel evalResultPanel,see_gradePanel,panel;
	private TextArea comField;
	private JTable resultsTable;
	private JScrollPane scrollPaneResult;
	private JTableHeader header;
	private JTextField belongsField;
	private JLabel lblChooseFromAlready;
	
	
	
	
	//Jframe's functions code
	Font font1 = new Font("Arial", Font.ITALIC, 24);
	Font font2 = new Font("Arial", Font.ITALIC, 20);


    
	//constructor for Jframe
	public EvaluatorGUI() {
		createEvaluatorGUI();
		createEvalEvents();
	}
	
	//log out and login return
	public JFrame closeFrame(JButton btn) {
		  int a = JOptionPane.showConfirmDialog(btn,"Are you sure?");
		  if(a == JOptionPane.YES_OPTION) {
			   dispose();
			   guiForDB window = new guiForDB();
			   window.getFrame().setVisible(true);
		  }
		  else return null;
		return null;
	}
	
	
	//method which inserts a grade into evaluation_info table
	private void gradeEmployee() {
		try {
			
		  if (!gradeField.getText().isEmpty()) {
				
			String stored = "{call gradeEmployee(?,?,?,?,?)}";
			cs = connection.prepareCall(stored);
			
			cs.setString(1, usr);
			cs.setString(2, employeeComboBox.getSelectedItem().toString());
			cs.setString(3, codeComboBox.getSelectedItem().toString());
			cs.setInt(4, Integer.parseInt(gradeField.getText()));
			cs.setString(5, comField.getText());
			
			cs.execute();
		    
			JOptionPane.showMessageDialog(null,"Succesfull grading.");
			cs.close();
			
		}
			
			else JOptionPane.showMessageDialog(null,"Invalid Option. Please give grade.");
		}
		catch(SQLException e){
		    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
		    	JOptionPane.showMessageDialog(null,"This is already a register.");
		      }
		finally {   gradeField.setText("");
                    comField.setText("");
		         }
	}
	
	
	//loads a table with the data from request_evaluation table
	private void loadRequestsTable() {

		try {
			  String stored = "{call returnRequests(?,?)}";
			 cs = connection.prepareCall(stored);
			 cs.setString(1, usr);
			 cs.setInt(2, requestComboBox.getSelectedIndex());
			
			 rs = cs.executeQuery();
			 
			 requestTable.setModel(DbUtils.resultSetToTableModel(rs));
			 header = requestTable.getTableHeader();
			 requestTable.getTableHeader().setFont(font2);
		     header.setBackground(Color.gray);
		      
			
			rs.close();
			cs.close();
		}
		catch(Exception ex) {ex.printStackTrace();}
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
            
			list.setModel(dlm);
			
			pst.close();
			rs.close();
		}
		catch(Exception ex) {ex.printStackTrace();}
	}

	
	//fills combo box with usernames and jobcodes to be graded
    private void fillReqUsr_JobsComboBox() {

			try {
				String stored = "{call returnRequests(?,?)}";
				cs = connection.prepareCall(stored);
				cs.setString(1, usr);
				cs.setInt(2, 1);
				
				rs= cs.executeQuery();
				 
				 while(rs.next()) {
					
				 codeComboBox.addItem(rs.getString("code"));
				 employeeComboBox.addItem(rs.getString("username"));
				 }
		
			      
				
				rs.close();
				cs.close();
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
		
	//loads company jobs
	private void loadJobTable() {
		
		try {
			
			 String stored = "{call ReturnJobs(?, ?)}";
			 cs = connection.prepareCall(stored);
			 cs.setString(1, usr);
			 cs.setInt(2, jobSelection.getSelectedIndex());
			 
			 rs = cs.executeQuery();
			 jobTable.setModel(DbUtils.resultSetToTableModel(rs));
			 header = jobTable.getTableHeader();
			 jobTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		     header.setBackground(Color.gray);
			 
			 rs.close();
			 cs.close();
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	
	//updates user's profile stats
	private void updateUserInfo() {
		
	 try {
		 
		 String query = "UPDATE user SET password = ? , name = ? , surname = ? , register_date = ? , email = ? WHERE username = ? ";
		 pst = connection.prepareStatement(query);
		 
		 pst.setString(1, String.valueOf(chgPsdField.getPassword()));
		 pst.setString(2, chgNameField.getText());
		 pst.setString(3, chgSurField.getText());
		 pst.setObject(4, chgDateField.getText());
		 pst.setString(5, chgEmailField.getText());
		 pst.setString(6, usr);
		 
		 pst.execute();
		 
		 JOptionPane.showMessageDialog(null,"Succesfull user info updapte.");
		 
		 pst.close();
		 
	 } catch(Exception e) { JOptionPane.showMessageDialog(null, e); }
	 
	  finally {
		  loadUserStatus();
		  
		  chgPsdField.setText("");
		  chgNameField.setText("");
		  chgSurField.setText("");
		  chgDateField.setText("");
		  chgEmailField.setText("");
	  }
	}
	
	
	//finalized the evaluation requests which have all field completed for a specific job id
	private void finalizeRequest() {
		
		try {
			
		 String stored = "{call FinalizeRequest(?, ?)}";
		 cs = connection.prepareCall(stored);
	     
		 cs.setInt(1, Integer.parseInt(resultsJobCodeComboBox.getSelectedItem().toString()));
		 cs.setString(2,usr);
		 
		 cs.execute();  
		 JOptionPane.showMessageDialog(null,"Succesfull request finalization.");
		 
		 cs.close();
		 
		}
	    catch(SQLException e) {
		    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
		    	JOptionPane.showMessageDialog(null,"This is already a register."); }
		
		finally {loadResultsTable();}
	}
	
	
	//methos which updates an already registerd job
	private void updateJob() {
		
		try {
			  if(jobCodesComboBox.getSelectedIndex() != 0) {
				 String stored = "{call updateJob(?, ?, ?, ?, ?, ?)}";
				 cs = connection.prepareCall(stored);
				 
				 cs.setInt(1, Integer.parseInt(jobCodesComboBox.getSelectedItem().toString()));  
				 cs.setString(2, startdateField.getText());
				 cs.setFloat(3,  Float.parseFloat(salaryField.getText()));
				 cs.setString(4, jobTitleField.getText() );
		         cs.setString(5, edraField.getText());
		         cs.setString(6, deadlineField.getText());
		         
				 cs.execute();
				
				 JOptionPane.showMessageDialog(null,"Succesfull Job Update.");
				
				 cs.close();
			   }
			  
			  else JOptionPane.showMessageDialog(null, "Invalid Option");
			  
			  
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,"Invalid Option.");
					e.printStackTrace(); }
		          finally {loadJobTable();
		                  startdateField.setText("");
					      salaryField.setText("");
					      jobTitleField.setText("");
					      edraField.setText("");
					      deadlineField.setText("");
					     }
		
		        
	}
	
	
	//connects a job with it's requirements by insterting them into needs table
	private void jobNeedsRegister() {
		try {
			String temp = fieldComboBox.getSelectedItem().toString();
			 if ( temp.contains("-")) { temp = temp.substring(0, temp.indexOf("-"));   }
				 String stored = "{call InsertIntoNeeds(?,?)}";
				 cs = connection.prepareCall(stored);
				 
				 cs.setInt(1, Integer.parseInt(jobCodesNeedsCb.getSelectedItem().toString()));
				 cs.setString(2, temp);
		         
				 cs.execute();
				 JOptionPane.showMessageDialog(null, "Succesfull job requirements.");
				 cs.close();
			  
				}
		         catch(SQLException ex){
		         if(ex.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
		    	 JOptionPane.showMessageDialog(null,"This is already a register.");}
		         catch(Exception e) {JOptionPane.showMessageDialog(null, "Invalid Option.");}
		         
		          finally {  loadJobTable(); }
		
	}
	
	
	//calls procedure and make a new user register
	private void registerField(){
		try {
			 String stored = "{call regNewField(?,?,?)}"; 
			 cs = connection.prepareCall(stored);
					 
			 cs.setString(1, titleField.getText());
			 cs.setString(2, descrField.getText());
			 cs.setString(3, belongsField.getText());
					 
			 cs.execute();
			         
			 JOptionPane.showMessageDialog(null,"Succesfull title/field register.");
					 
			  cs.close();
				       
			}   catch(SQLException e){
					if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
					JOptionPane.showMessageDialog(null,"This is already a register."); }
		        catch(Exception e) {JOptionPane.showMessageDialog(null, "Invalid Option.");}
		
				      finally {
				    	 titleField.setText("");
						 descrField.setText("");
						 belongsField.setText("");
						      }		
				}

	
	
	//loads and fills the text fields in jobs tab for a specific job id
	private void loadValuesForUpdate() {
		try {
		 String query = "SELECT * from job where code = ? ";
		 pst = connection.prepareStatement(query);
		 
		 pst.setInt(1, Integer.parseInt(jobCodesComboBox.getSelectedItem().toString())); 
		 rs = pst.executeQuery();
		 
		 while(rs.next()) {
			 startdateField.setText(rs.getString("start_date"));
			 salaryField.setText(rs.getString("salary"));
			 jobTitleField.setText(rs.getString("pos_title"));
			 edraField.setText(rs.getString("edra"));
			 deadlineField.setText(rs.getString("deadline"));
		 }
		 pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//method for new job registration
	private void registerJob()  {
		
	try {
			
	  if( !startdateField.getText().isEmpty() && !jobTitleField.getText().isEmpty() ) {
		 String stored = "{call InsertIntoJob(?, ?, ?, ?, ?, ?)}";
		 CallableStatement cp = connection.prepareCall(stored);
			
		 cp.setString(1, startdateField.getText());
		 cp.setFloat(2,  Float.parseFloat(salaryField.getText())); 
		 cp.setString(3, jobTitleField.getText() );
         cp.setString(4, edraField.getText());
         cp.setString(5, usr);
         cp.setString(6, deadlineField.getText());
         
		 cp.execute();
		
		 JOptionPane.showMessageDialog(null,"Succesfull job register.");
		 
		 cp.close();

		}
	  else JOptionPane.showMessageDialog(null,"Invalid Option.");
		 
		} catch(SQLException e){
		    if(e.getErrorCode() == guiForDB.MYSQL_DUPLICATE_PK )
		    	JOptionPane.showMessageDialog(null,"This is already a register."); }
	
	      finally {
	    	  startdateField.setText("");
			  salaryField.setText("");
			  jobTitleField.setText("");
			  edraField.setText("");
			  deadlineField.setText(""); 
			  loadJobTable(); 
			    }
	}
	

	
	//fills a combo box with all the job ids
	private void fillJobCodesComboBox() {
		
		try {
			 
			 String query = "SELECT code FROM job WHERE evaluator = ?";
			 pst = connection.prepareStatement(query);
			 
		     pst.setString(1, usr);
			 rs = pst.executeQuery();
			 jobCodesComboBox.addItem("");
			 jobCodesNeedsCb.addItem("");
			 while(rs.next()) {
			   jobCodesComboBox.addItem(rs.getString("code"));
			   resultsJobCodeComboBox.addItem(rs.getString("code"));
			   jobCodesNeedsCb.addItem(rs.getString("code"));
			 }
			 
			 pst.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }	
		
	}
     
	
	
	//fills a combo box with company's work fields
    private void fillJobFieldComboBox() {

		try {
			 String query = "SELECT DISTINCT CONCAT(title,'-',belongs_to) FROM field";
			 pst = connection.prepareStatement(query);
			 rs = pst.executeQuery();
			 fieldComboBox.removeAllItems();
			 while(rs.next()) {
			 fieldComboBox.addItem(rs.getString(1)); // 1 indicates the number of column.
			 }
			
			 pst.close();
			 
		 }catch(Exception e) { JOptionPane.showMessageDialog(null, e); }	
		
	}


    
    //loads a table with either with the finalized evaluation requests or unfinalized for a specific job id
    private void loadResultsTable() {
    	try {
			
			 String stored = "{call ShowCandidates(?, ?, ?)}";
			 cs = connection.prepareCall(stored);
			 cs.setString(1, usr);
			 cs.setInt(2, Integer.parseInt(resultsJobCodeComboBox.getSelectedItem().toString()));
			 cs.setInt(3, resultComboBox.getSelectedIndex());
			 
			 rs = cs.executeQuery();
			 resultsTable.setModel(DbUtils.resultSetToTableModel(rs));
			 header = resultsTable.getTableHeader();
			 resultsTable.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 16));
		     header.setBackground(Color.gray);
			 
			 rs.close();
			 cs.close();
		}
		catch(Exception ex) {ex.printStackTrace();}
    	
    }
    
    
    
 // methos which contains all events for evaluatorGUI class
 	private void createEvalEvents() {
 		
 		//finishButton, updates user's info.
 		btnFinish.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				updateUserInfo();
 			}
 		});
 		
 		showPsd.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				if (showPsd.isSelected()) {
 				      chgPsdField.setEchoChar((char)0); 
 				   } else {
 				      chgPsdField.setEchoChar('*');
 				   }
 			}
 		});
 		
 		showPsdBtn.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				if(showPsdBtn.isSelected()){    
 					JOptionPane.showMessageDialog(null,"Your Password is: "+psd);    
 					}    
 			}
 		});
 		
 		jobSelection.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				loadJobTable();
 			}
 		});
 		
 		regJobButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 					registerJob();
 			}
 		});
 		
 		updateBtn.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				updateJob();
 				jobCodesComboBox.setSelectedIndex(0);
 			}
 		});
 		
 		jobCodesComboBox.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				if(jobCodesComboBox.getSelectedIndex() == 0) {
 				 startdateField.setText("");
 				 salaryField.setText("");
 				 jobTitleField.setText("");
 				 edraField.setText("");
 				 deadlineField.setText("");
 				 titleField.setText("");
 				 descrField.setText("");}
 				else loadValuesForUpdate();
 			}
 		});
 		
 		gradeBtn.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				gradeEmployee();
 				loadResultsTable();
 				fillReqUsr_JobsComboBox();
 				loadRequestsTable();
 			}
 		});
 		
 		
 		resultComboBox.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				loadResultsTable();
 			}
 		});
 		
 		
 		resultsJobCodeComboBox.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				loadResultsTable();
 			}
 		});
 		
 		
 		finalizeBtn.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				finalizeRequest();
 			}
 		});
 		
 		requestComboBox.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				loadRequestsTable();
 			}
 		});
 		
 		finishRegButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fieldComboBox.getSelectedIndex() == -1) {
					registerField();
					fillJobFieldComboBox();
					
				}
				else if (fieldComboBox.getSelectedIndex() >= 0){
					jobNeedsRegister();
					fieldComboBox.setSelectedIndex(-1);
					jobCodesNeedsCb.setSelectedIndex(-1);}
				
				else JOptionPane.showMessageDialog(null,"Invalid Option.");
			}
		});
 		
 		logOutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeFrame(logOutBtn);
			}
		});
 		
 	}
 	
 	
    //THE BIG BOSS
    private void createEvaluatorGUI(){

		
		connection = dbCon.connectToDB();
        
        //Jframe appearance code
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1434, 664);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		//code for evaluator's Workspace tab display
		JTabbedPane evaluatortabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		
		//instances and objects for evaluator's workspace
		profPanel = new JPanel();
		profPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		evaluatortabbedPane.addTab("Profile", null, profPanel, null);
		profPanel.setLayout(null);
		
		settingsPanel = new JPanel();
		settingsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Profile Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		settingsPanel.setBounds(661, 13, 604, 483);
		profPanel.add(settingsPanel);
		settingsPanel.setLayout(null);
		
		changePsdLabel = new JLabel("Password to:");
		changePsdLabel.setFont(font2);
		changePsdLabel.setBounds(22, 73, 135, 40);
		settingsPanel.add(changePsdLabel);
		
		chgNameLabel = new JLabel("Name to:");
		chgNameLabel.setFont(font2);
		chgNameLabel.setBounds(22, 135, 88, 29);
		settingsPanel.add(chgNameLabel);
		
		chgSurLabel = new JLabel("Surname to:");
		chgSurLabel.setFont(font2);
		chgSurLabel.setBounds(22, 189, 135, 29);
		settingsPanel.add(chgSurLabel);
		
		chgDateLabel = new JLabel("Register date to:");
		chgDateLabel.setFont(font2);
		chgDateLabel.setBounds(22, 245, 146, 29);
		settingsPanel.add(chgDateLabel);
		
		chgNameField = new JTextField();
		chgNameField.setFont(font2);
		chgNameField.setBounds(183, 137, 207, 41);
		settingsPanel.add(chgNameField);
		chgNameField.setColumns(10);
		
		chgPsdField = new JPasswordField();
		chgPsdField.setFont(font2);
		chgPsdField.setBounds(183, 81, 207, 40);
		settingsPanel.add(chgPsdField);
		
		change = new JLabel("Change");
		change.setBackground(Color.WHITE);
		change.setFont(font1);
		change.setBounds(22, 34, 131, 29);
		settingsPanel.add(change);
		
		chgSurField = new JTextField();
		chgSurField.setFont(font2);
		chgSurField.setBounds(183, 191, 207, 43);
		settingsPanel.add(chgSurField);
		chgSurField.setColumns(10);
		
		chgDateField = new JTextField();
		chgDateField.setFont(font2);
		chgDateField.setBounds(183, 247, 207, 40);
		settingsPanel.add(chgDateField);
		chgDateField.setColumns(10);
		
		chgEmailField = new JTextField();
		chgEmailField.setFont(font2);
		chgEmailField.setColumns(10);
		chgEmailField.setBounds(183, 304, 207, 40);
		settingsPanel.add(chgEmailField);
		
		chgEmailLabel = new JLabel("Email to:");
		chgEmailLabel.setFont(font2);
		chgEmailLabel.setBounds(22, 305, 104, 29);
		settingsPanel.add(chgEmailLabel);
		
		btnFinish = new JButton("Finish");
		btnFinish.setFont(font2);
		btnFinish.setBounds(146, 408, 170, 48);
		settingsPanel.add(btnFinish);
		
		showPsd = new JCheckBox("Reveal");
		showPsd.setBounds(439, 83, 113, 25);
		settingsPanel.add(showPsd);
		
		statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder(null, "General Profile Status", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		statusPanel.setBounds(12, 13, 587, 483);
		profPanel.add(statusPanel);
		statusPanel.setLayout(null);
		
		//for Jlist
		list = new JList<Object>();
		list.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		list.setBackground(Color.LIGHT_GRAY);
		list.setBounds(12, 33, 454, 437);
		statusPanel.add(list);
		
		showPsdBtn = new JRadioButton("Show");
		showPsdBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		showPsdBtn.setBounds(474, 72, 86, 32);
		statusPanel.add(showPsdBtn);
		
		
		jobsPanel = new JPanel();
		evaluatortabbedPane.addTab("Jobs", null, jobsPanel, null);
		jobsPanel.setLayout(null);
		
		evalPanel = new JPanel();
		evaluatortabbedPane.addTab("Evaluations", null, evalPanel, null);
		evalPanel.setLayout(null);
		
		scrollJob = new JScrollPane();
		scrollJob.setBounds(484, 69, 881, 244);
		jobsPanel.add(scrollJob);
		
		jobTable = new JTable();
		jobTable.setFont(new Font("Arial", Font.ITALIC, 14));
		scrollJob.setViewportView(jobTable);
		
		String jobChoice [] = {"All Jobs", "My Jobs"};
		jobSelection = new JComboBox<Object>(jobChoice);
		jobSelection.setFont(font2);
		jobSelection.setBounds(790, 13, 119, 45);
		jobsPanel.add(jobSelection);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 456, 521);
		jobsPanel.add(tabbedPane);
		
		registerPanel = new JPanel();
		tabbedPane.addTab("Register Job", null, registerPanel, null);
		registerPanel.setLayout(null);
		
		startdataLabel = new JLabel("Start Date:");
		startdataLabel.setBounds(13, 13, 94, 27);
		registerPanel.add(startdataLabel);
		startdataLabel.setFont(font2);
		
		salaryLabel = new JLabel("Salary:");
		salaryLabel.setBounds(12, 75, 63, 24);
		registerPanel.add(salaryLabel);
		salaryLabel.setFont(font2);
		
		startdateField = new JTextField();
		startdateField.setBounds(132, 10, 176, 30);
		registerPanel.add(startdateField);
		startdateField.setFont(font2);
		startdateField.setColumns(10);
		
		salaryField = new JTextField();
		salaryField.setBounds(132, 72, 176, 30);
		registerPanel.add(salaryField);
		salaryField.setFont(font2);
		salaryField.setColumns(10);
		
		postitleLabel = new JLabel("Title:");
		postitleLabel.setBounds(13, 133, 44, 24);
		registerPanel.add(postitleLabel);
		postitleLabel.setFont(font2);
		
		jobTitleField = new JTextField();
		jobTitleField.setBounds(132, 130, 176, 30);
		registerPanel.add(jobTitleField);
		jobTitleField.setFont(font2);
		jobTitleField.setColumns(10);
		
		edraLabel = new JLabel("Head office:");
		edraLabel.setBounds(13, 196, 108, 24);
		registerPanel.add(edraLabel);
		edraLabel.setFont(font2);
		
		edraField = new JTextField();
		edraField.setBounds(132, 193, 176, 30);
		registerPanel.add(edraField);
		edraField.setFont(font2);
		edraField.setColumns(10);
		
		deadlineLabel = new JLabel("Deadline:");
		deadlineLabel.setBounds(12, 255, 85, 24);
		registerPanel.add(deadlineLabel);
		deadlineLabel.setFont(font2);
		
		deadlineField = new JTextField();
		deadlineField.setBounds(132, 252, 176, 30);
		registerPanel.add(deadlineField);
		deadlineField.setFont(font2);
		deadlineField.setColumns(10);
		
		updateBtn = new JButton("Update");
		updateBtn.setBounds(299, 399, 140, 45);
		registerPanel.add(updateBtn);
		updateBtn.setFont(font1);
		
		jobCodesComboBox = new JComboBox<String>();
		jobCodesComboBox.setBounds(219, 400, 55, 45);
		registerPanel.add(jobCodesComboBox);
		jobCodesComboBox.setFont(font2);
		
		
		regJobButton = new JButton("Register");
		regJobButton.setBounds(13, 399, 131, 45);
		registerPanel.add(regJobButton);
		regJobButton.setFont(font1);
		
		panel = new JPanel();
		tabbedPane.addTab("Register Category/Title", null, panel, null);
		panel.setLayout(null);
		
		fieldTitleLabel = new JLabel("Field Title:");
		fieldTitleLabel.setBounds(12, 72, 108, 34);
		panel.add(fieldTitleLabel);
		fieldTitleLabel.setFont(font2);
		
		titleField = new JTextField();
		titleField.setBounds(209, 225, 176, 34);
		panel.add(titleField);
		titleField.setFont(font2);
		titleField.setColumns(10);
		
		descrField = new JTextField();
		descrField.setBounds(209, 288, 176, 34);
		panel.add(descrField);
		descrField.setFont(font2);
		descrField.setColumns(10);
		
		fieldComboBox = new JComboBox<String>();
		
		fieldComboBox.setBounds(113, 74, 261, 34);
		panel.add(fieldComboBox);
		
		lblRegisterNew = new JLabel("Register New:");
		lblRegisterNew.setFont(font1);
		lblRegisterNew.setBounds(101, 167, 164, 34);
		panel.add(lblRegisterNew);
		
		regTitleLbl = new JLabel("Field Title:");
		regTitleLbl.setFont(font2);
		regTitleLbl.setBounds(12, 230, 101, 24);
		panel.add(regTitleLbl);
		
		regDescrLbl = new JLabel("Description:");
		regDescrLbl.setFont(font2);
		regDescrLbl.setBounds(12, 293, 108, 24);
		panel.add(regDescrLbl);
		
		regBelLbl = new JLabel("Belongs:");
		regBelLbl.setFont(font2);
		regBelLbl.setBounds(12, 355, 108, 24);
		panel.add(regBelLbl);
		
		belongsField = new JTextField();
		belongsField.setFont(font2);
		belongsField.setColumns(10);
		belongsField.setBounds(209, 355, 176, 34);
		panel.add(belongsField);
		
		lblChooseFromAlready = new JLabel("Choose from already Registered:");
		lblChooseFromAlready.setFont(font1);
		lblChooseFromAlready.setBounds(34, 13, 373, 34);
		panel.add(lblChooseFromAlready);
		
		finishRegButton = new JButton("Finish register.");
		finishRegButton.setFont(new Font("Arial", Font.ITALIC, 16));
		finishRegButton.setBounds(113, 437, 152, 41);
		panel.add(finishRegButton);
		
		jobCodesNeedsCb = new JComboBox<String>();
		jobCodesNeedsCb.setFont(font2);
		jobCodesNeedsCb.setBounds(386, 74, 53, 34);
		panel.add(jobCodesNeedsCb);
		
		see_gradePanel = new JPanel();
		see_gradePanel.setBorder(new TitledBorder(null, "See/Grade Evaluation Requests", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		see_gradePanel.setBounds(12, 13, 653, 492);
		evalPanel.add(see_gradePanel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 70, 613, 135);
		
		requestTable = new JTable();
		requestTable.setForeground(Color.BLACK);
		requestTable.setFont(new Font("Arial", Font.ITALIC, 14));
		scrollPane.setViewportView(requestTable);
		
		comField = new TextArea();
		comField.setBounds(269, 257, 374, 175);
		comField.setFont(font2);
		
		evalGradeLabel = new JLabel("Give Grade:");
		evalGradeLabel.setBounds(44, 227, 124, 24);
		evalGradeLabel.setFont(font2);
		
		gradeField = new JTextField();
		gradeField.setBounds(44, 271, 124, 33);
		gradeField.setFont(font2);
		
		gradeBtn = new JButton("Grade");
		gradeBtn.setBounds(32, 432, 124, 43);
		gradeBtn.setFont(font2);
		
		comLabel = new JLabel("Any comments?");
		comLabel.setBounds(385, 227, 150, 24);
		comLabel.setFont(font2);
		
		codeComboBox = new JComboBox<String>();
		codeComboBox.setBounds(18, 338, 52, 35);
		codeComboBox.setFont(font2);
		
		employeeComboBox = new JComboBox<String>();
		employeeComboBox.setBounds(102, 338, 125, 35);
		employeeComboBox.setFont(font2);
		see_gradePanel.setLayout(null);
		see_gradePanel.add(codeComboBox);
		see_gradePanel.add(employeeComboBox);
		see_gradePanel.add(evalGradeLabel);
		see_gradePanel.add(gradeField);
		see_gradePanel.add(gradeBtn);
		see_gradePanel.add(comLabel);
		see_gradePanel.add(comField);
		see_gradePanel.add(scrollPane);
		
		
		String[] req = {"All Requests", "Requests with no grade"};
		requestComboBox = new JComboBox(req);
		requestComboBox.setFont(new Font("Arial", Font.ITALIC, 18));
		requestComboBox.setBounds(187, 24, 223, 35);
		see_gradePanel.add(requestComboBox);
		
		evalResultPanel = new JPanel();
		evalResultPanel.setBorder(new TitledBorder(null, "See Evaluation Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		evalResultPanel.setBounds(703, 13, 647, 492);
		evalPanel.add(evalResultPanel);
		evalResultPanel.setLayout(null);
		
		scrollPaneResult = new JScrollPane();
		scrollPaneResult.setBounds(12, 107, 623, 151);
		evalResultPanel.add(scrollPaneResult);
		
		resultsTable = new JTable();
		resultsTable.setFont(new Font("Arial", Font.ITALIC, 15));
		scrollPaneResult.setViewportView(resultsTable);
		
		String[] opt = {"Unfinalized Results","Finalized Results"};
		resultComboBox = new JComboBox(opt);
		resultComboBox.setFont(font2);
		resultComboBox.setBounds(162, 27, 217, 37);
		evalResultPanel.add(resultComboBox);
		
		resultsJobCodeComboBox = new JComboBox<String>();
		resultsJobCodeComboBox.setFont(font2);
		resultsJobCodeComboBox.setBounds(413, 27, 59, 36);
		evalResultPanel.add(resultsJobCodeComboBox);
		
		finalizeBtn = new JButton("Finalize");
		finalizeBtn.setFont(font2);
		finalizeBtn.setBounds(215, 309, 164, 37);
		evalResultPanel.add(finalizeBtn);
		
		logOutBtn = new JButton("Log out");
		logOutBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addComponent(evaluatortabbedPane, GroupLayout.DEFAULT_SIZE, 1382, Short.MAX_VALUE)
					.addGap(12))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(1283, Short.MAX_VALUE)
					.addComponent(logOutBtn)
					.addGap(26))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(logOutBtn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(evaluatortabbedPane, GroupLayout.PREFERRED_SIZE, 564, GroupLayout.PREFERRED_SIZE))
		);
		
		contentPane.setLayout(gl_contentPane);
		
		//bunch of pre called methods to set up things nice
		loadRequestsTable();
		
		loadUserStatus();
		
		loadJobTable();
		
		fillJobCodesComboBox();
		
		fillJobFieldComboBox();
		
		loadResultsTable();
		
		fillReqUsr_JobsComboBox();
	}
}//end of class evaluatorGUI
