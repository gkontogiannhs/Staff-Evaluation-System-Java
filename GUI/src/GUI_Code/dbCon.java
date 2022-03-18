package DB_GUI;

import java.sql.*;


public class dbCon {
     
	  //class instances
	  private static final String DB_URL = "jdbc:mysql://localhost:3306/gdh";
	  private static final String USERNAME = "root";
	  private static final String PASSWORD = "";
	  private static final String driver = "com.mysql.cj.jdbc.Driver";
	  
	  private static Connection con;
	  
	
	//constructor
	  public dbCon() {connectToDB();}
	
	  
	//method which establishes connection with  DB
	public static Connection connectToDB() {
		try {
		 Class.forName(driver);
		 con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
		 System.out.println("Succesfull Connection..");
		 return con;
		    }
		 catch(Exception e) { 
			  System.out.println("Unsuccesful connection with db.");
		      System.out.println("Error: "+ e);
		      e.printStackTrace();  
		                    }		
		return null;
	}
		  	 
	
}//end of class dbCon
