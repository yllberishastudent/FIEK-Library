package controllers;

import java.lang.System.Logger.Level;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.DatabaseHandler;

public class addBookController {

	  DatabaseHandler databaseHandler;
	    @FXML
	    private AnchorPane rootPane;
	    @FXML
	    private TextField title;
	    @FXML
	    private TextField id;
	    @FXML
	    private TextField author;
	    @FXML
	    private TextField publisher;
	    @FXML
	    private TextField quantity;
	    @FXML
	    private Button saveBtn;
	    @FXML
	    private Button cancelBtn;
	    @FXML
	    private CheckBox check;
	    @FXML
	    private AnchorPane rootPane1;
	    
	    @FXML
	    private void addBook(ActionEvent event) {

	        String bookID = id.getText();
	        String bookAuthor = author.getText();
	        String bookName = title.getText();
	        String bookPublisher = publisher.getText();

	        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setHeaderText(null);
	            alert.setContentText("Please Enter in all fields");
	            alert.showAndWait();
	            return;
	        }

	        String qu = "INSERT INTO BOOK VALUES ("
	                + "'" + bookID + "',"
	                + "'" + bookName + "',"
	                + "'" + bookAuthor + "',"
	                + "'" + bookPublisher + "',"
	                + "" + "true" + ""
	                + ")";
	        System.out.println(qu);
	        if (databaseHandler.execAction(qu)) {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setHeaderText(null);
	            alert.setContentText("Success");
	            alert.showAndWait();
	        } else //Error
	        {
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setHeaderText(null);
	            alert.setContentText("Failed");
	            alert.showAndWait();
	        }
	         }
	         
	      

	    @FXML
	    private void cancel(ActionEvent event) {
	    	   Stage stage = (Stage) rootPane.getScene().getWindow();
	           stage.close();
	       }

	       private void checkData() {
	           String qu = "SELECT title FROM BOOK";
	           ResultSet rs = databaseHandler.execQuery(qu);
	           try {
	               while(rs.next()){
	                   String titlex = rs.getString("title");
	                   System.out.println(titlex);
	               }
	           } catch (SQLException ex) {
	        //	   Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE, null, ex);
	        	   //todo
	           }
	    }
	    
	

	    public void initialize(URL url, ResourceBundle rb) {
	        // TODO
	        databaseHandler = new DatabaseHandler();
	        checkData();
	    }
}
