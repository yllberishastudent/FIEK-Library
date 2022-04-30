package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;



public class databaseConn {

		private final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
		private final String IP = "localhost:3306";	
		private final String DATATABASE = "admin";
		private final String USERNAME = "root";
		private final String PASSWORD = "admin";
	
		private Connection connection;
		
		
		public static databaseConn getConnection() {
			return new databaseConn();
			
		}
		
		
		private databaseConn() {
			
		 	this.connection = this.innitConnection();
		
		}
		
		private Connection innitConnection() {
			
			try {
				Class.forName(DRIVER_NAME);	
				
				// "jdbc:mysql://{ip}/{database}" , {username}, {Password}
				return DriverManager.getConnection("jdbc:mysql://localhost:3306/admin", "root", "admin");
				
				
				
			}
			catch (Exception e) {}
			System.out.println("Connetion failed...");
			return null;
		}
	
	public void executeQuery(String query) {
		try {
			Statement statement = this.connection.createStatement();
			//statement.execute(query);
			
			ResultSet Results = statement.executeQuery(query);
			
			while(Results.next()) {
				System.out.println("ID" + Results.getInt("idUserAccount"));
				System.out.println("Emri: " + Results.getString("firstName"));
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}	
	
	public static void main(String args[]) {
		databaseConn connection = new databaseConn();
		String query = "Select * From userAccount";
		connection.executeQuery(query);
	}
	
		
		
}



