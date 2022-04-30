package repository;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import java.sql.*;

// This class handles the database connection

public class DatabaseHandler {

    private static final String databaseName = "admin";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/admin";
    // Variables used for connecting to the database and creating statements
    // Change these values to your mySQL values
    private static DatabaseHandler handler = null;
    private static Connection conn = null;
    private static Statement stmt = null;
    
    public DatabaseHandler() {
    	createConnection();
    	setupBookTable();
    	}
    
    
    void createConnection() {
    	try {
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/admin", "root", "admin");
    	}
    	catch(Exception e) 
    	{
    		e.printStackTrace();
    	}
    }
    void setupBookTable() {
    	String table_name = "BOOK";
    	try {
    		stmt = conn.createStatement();
    		
    		DatabaseMetaData dmb = conn.getMetaData();
    		ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);
    		
    		if(tables.next()) {
    			System.out.println("Talbe"+ table_name + "already exists");
    			
    		}
    		else {
    			stmt.execute("Create Table" + table_name + "(\r\n"
    					+ "Bookid varchar(200) not null,\r\n"
    					+ "title varchar(200) not null,\r\n"
    					+ "author varchar(200) not null,\r\n"
    					+ "publisher varchar(200) not null,\r\n"
    					+ "quantity int not null,\r\n"
    					+ "isAvail boolean default true,\r\n"
    					+ "primary key(Bookid) );" 
    			
    			);
    			
    		}
    	}catch(SQLException e) {
    		System.err.println(e.getMessage() + "---setup");
    	}
    }
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }


    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }
}