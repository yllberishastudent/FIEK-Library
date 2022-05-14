package controllers;

import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repositories.DatabaseConnection;
import repositories.DatabaseHandler;

public class loginController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
    	   Stage stage = (Stage) cancelButton.getScene().getWindow();
           stage.close();
       }

    @FXML
    void loginButtonOnAction(ActionEvent event) {
    	   if(!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()) {
               // loginMessageLabel.setText("You try to login!");
               DatabaseHandler connectNow = new DatabaseHandler();
               String verifyLogin = "select count(1) from userAccount where username = '" + usernameTextField.getText() + "' and pass = '" + passwordPasswordField.getText() + "'";
               try {
                   
                   ResultSet queryResult = connectNow.execQuery(verifyLogin);

                   while(queryResult.next()) {
                       if(queryResult.getInt(1) == 1) {
                           //loginMessageLabel.setText("Welcome!");
                           Parent parent = FXMLLoader.load(getClass().getResource("/views/main-screen.fxml"));
                           Scene scene = new Scene(parent);
                           Stage primaryStage=new Stage();
                           primaryStage.initStyle(StageStyle.DECORATED);
                           primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                           primaryStage.setScene(scene);
                           primaryStage.setTitle("Library Management System");
                           primaryStage.getIcons().add(new Image("https://static.thenounproject.com/png/3314579-200.png"));
                           primaryStage.show();
                       } else {
                           loginMessageLabel.setText("Invalid Login. Please try again.");
                       }
                   }
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           } else {
               loginMessageLabel.setText("Please enter username and password.");
           }
       }

}
