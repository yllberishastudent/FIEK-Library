package repositories;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import repositories.DatabaseHandler;
import controllers.booklistController;
import controllers.viewMembersController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import models.Book;

public class DatabaseHandler {

	 private static final String databaseName = "admin";
	    private static final String DB_URL = "jdbc:mysql://127.0.0.1/" + databaseName;
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
	    	String table_name = "addbook";
	    	try {
	    		stmt = conn.createStatement();
	    		
	    		DatabaseMetaData dmb = conn.getMetaData();
	    		ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);
	    		
	    		if(tables.next()) {
	    			System.out.println("database responded");
	    			
	    		}
	    		else {
	    			stmt.execute("Create Table" + table_name + "(\r\n"
	    					+ "Bookid varchar(200) not null,\n"
	    					+ "title varchar(200) not null,\n"
	    					+ "author varchar(200) not null,\n"
	    					+ "publisher varchar(200) not null,\n"
	    					+ "quantity int not null,\n"
	    					+ "isAvail boolean default true,\n"
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

	    public boolean deleteMember(models.Member member) {
	        try {
	            String deleteStatement = "Delete from addMember where memberID = ?";
	            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
	            stmt.setString(1, member.getMemberID());
	            int res = stmt.executeUpdate();
	            if (res == 1) {
	                return true;
	            }
	        } catch (SQLException ex) {
	            //Logger.getLogger(DatabaseHandler.class.getName().log(Level.SEVERE,null,ex));
	        }
	        return false;
	    }
	    public boolean deleteBook(models.Book book) {
	        try {
	            String deleteStatement = "Delete from addBook where Bookid = ?";
	            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
	            stmt.setString(1, book.getBookID());
	            int res = stmt.executeUpdate();
	            if (res == 1) {
	                return true;
	            }
	        } catch (SQLException ex) {
	            //Logger.getLogger(DatabaseHandler.class.getName().log(Level.SEVERE,null,ex));
	        }
	        return false;
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
	    public static DatabaseHandler getInstance() {
	        if (handler == null) {
	            handler = new DatabaseHandler();
	        }
	        return handler;
	    }
	    
	    
	    public boolean updateMember(models.Member member) {
	        String update = "UPDATE addMember SET name =? , email = ? , phone = ? where memberID = ?";
	        try {
	            PreparedStatement stmt = conn.prepareStatement(update);
	            stmt.setString(1, member.getName());
	            stmt.setString(2, member.getEmail());
	            stmt.setString(3, member.getPhone());
	            stmt.setString(4, member.getMemberID());


	            int res = stmt.executeUpdate();

	            return (res > 0);

	        } catch (SQLException throwables) {
	            throwables.printStackTrace();
	        }
	        return false;
	    }
	    public boolean updateBook(models.Book book) {
	        String update = "UPDATE addBook SET title =? , author = ? , publisher = ?,quantity = ? where Bookid = ?";
	        try {
	            PreparedStatement stmt = conn.prepareStatement(update);
	            stmt.setString(1, book.getTitle());
	            stmt.setString(2, book.getAuthor());
	            stmt.setString(3, book.getPublisher());
	            stmt.setInt(4, book.getQuantity());
	            stmt.setString(5, book.getBookID());


	            int res = stmt.executeUpdate();

	            return (res > 0);

	        } catch (SQLException throwables) {
	            throwables.printStackTrace();
	        }
	        return false;
	    }
	    
	    // Setting up the table for issued books
	    void setupIssuedBooksTable() {
	        String TABLE_NAME = "issuedBooks";
	        try {
	            stmt = conn.createStatement();
	            DatabaseMetaData dbm = conn.getMetaData();
	            ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
	            if (tables.next()) {
	                System.out.println("Table " + TABLE_NAME + " already exists.");
	            } else {
	                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
	                        + " bookID varchar(200) not null ,"
	                        + " memberID varchar(200) not null ,"
	                        + " issueTime timestamp default CURRENT_TIMESTAMP,"
	                        + " renew_count integer default 0 ,"
	                        + " primary key(bookID,memberID),"
	                        + " foreign key(bookID) references addBook(Bookid),"
	                        + " foreign key(memberID) references addMember(memberID)"
	                        + ")");
	            }
	        } catch (SQLException ex) {
	            System.err.println(ex.getMessage() + " ...setupDatabase");
	        }
	    }
	   
	    // Graphs
	    public ObservableList<PieChart.Data> getBookGraphicStatistics() {
	        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
	        try {
	            String query1 = "SELECT COUNT(*) FROM addBook";
	            String query2 = "SELECT COUNT(*) FROM issuedBooks";

	            ResultSet rs = execQuery(query1);
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                data.add(new PieChart.Data("Total Books (" + count + ")", count));
	            }

	            ResultSet rs2 = execQuery(query2);
	            if (rs2.next()) {
	                int count = rs2.getInt(1);
	                data.add(new PieChart.Data("Issued Copies Of Books (" + count + ")", count));
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return data;
	    }


	    public ObservableList<PieChart.Data> getMemberGraphicStatistics() {
	        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
	        try {
	            String query1 = "SELECT COUNT(*) FROM addMember";
	            String query2 = "SELECT COUNT(DISTINCT memberID) FROM issuedBooks";

	            ResultSet rs = execQuery(query1);
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                data.add(new PieChart.Data("Total Members (" + count + ")", count));
	            }

	            ResultSet rs2 = execQuery(query2);
	            if (rs2.next()) {
	                int count = rs2.getInt(1);
	                data.add(new PieChart.Data("Members With Books (" + count + ")", count));
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return data;

	    }
	}