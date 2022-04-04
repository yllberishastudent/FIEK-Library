package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


public class databaseConn {

	// A class to connect to database , used for  login
	public static void main(String args[]) {
		
		databaseConn test = new databaseConn();
		test.getConnection();
	}

	    public  Connection databaseLink;

	    public  Connection getConnection() {
	        String databaseName = "admin";
	        String databaseUser = "root";
	        String databasePassword = "";
	        String url = "jdbc:mysql://localhost/" + databaseName;
	        
	        String query = "Select * from userAccount";

	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");   
	            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
		        Statement statement = databaseLink.createStatement();
		        ResultSet result = statement.executeQuery(query);
		        
		        		    while (result.next()) {
		        		        String UniversityData = "";
		        		        for (int i = 1; i <= 4; i++) {
		        		             UniversityData += result.getString(i) + ":";
		        		         System.out.println(UniversityData);
		        		        }
		        		        }
	        }
	        catch (Exception e) {
	            System.out.println("Connection Failed...");
	        
	        }
		    return null;
	    }
}


