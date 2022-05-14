package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//import controllers.viewMembersController.Member;
import repositories.DatabaseHandler;
import controllers.addMemberController;
import controllers.MainController;
import controllers.addBookController;
import models.Member;

public class viewMembersController  implements Initializable {

	  DatabaseHandler databaseHandler ;
	  ObservableList<Member> list = FXCollections.observableArrayList();
    @FXML
    private Button addNewBtn;

    @FXML
    private TableColumn<Member, String> emailColumn;

    @FXML
    private TableColumn<Member, String> genderColumn;

    @FXML
    private TableColumn<Member, String> idColumn;

    @FXML
    private TableColumn<Member, String> nameColumn;

    @FXML
    private TableColumn<Member, String> phoneColumn;

    @FXML
    private TableView<Member> tableView;

    @FXML
    void addNewMember(ActionEvent event) {
    	loadWindow("/views/addMember.fxml", "Add Member");
    }

    @FXML
    void deleteMemberOption(ActionEvent event) {
    	  Member selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
          if (selectedForDeletion == null) {
              //AlertMaker.showErrorMessage("No member selected", "Please select a member for deletion.");
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setHeaderText("ERROR");
              alert.setContentText("No member selected ! Please select a member for deletion.");
              alert.showAndWait();
              return;
          }
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Deleting Member");
          alert.setContentText("Are you sure you want to delete" + selectedForDeletion.getName()+"?");
          Optional<ButtonType> answer = alert.showAndWait();

          if(answer.get() == ButtonType.OK){
              //Delete Member
              Boolean result = DatabaseHandler.getInstance().deleteMember(selectedForDeletion);
              if(result){
                  Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                  alert2.setHeaderText("SUCCESS");
                  alert2.setContentText("Member Deleted!");
                  alert2.showAndWait();
                  list.remove(selectedForDeletion);
              }else{
                  Alert alert3 = new Alert(Alert.AlertType.ERROR);
                  alert3.setHeaderText("Error");
                  alert3.setContentText("Member could not be Deleted!");
                  alert3.showAndWait();
              }
          }else{
              Alert alert1 = new Alert(Alert.AlertType.ERROR);
              alert1.setHeaderText("ERROR");
              alert1.setContentText("Deletion Cancelled");
              alert1.showAndWait();
          }
    }

    @FXML
    void editMemberOption(ActionEvent event) {
    	 Member selectedForEdit = tableView.getSelectionModel().getSelectedItem();

         if (selectedForEdit == null) {
             //AlertMaker.showErrorMessage("No member selected", "Please select a member for deletion.");
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setHeaderText("ERROR");
             alert.setContentText("No member selected ! Please select a member for edit-ing.");
             alert.showAndWait();
             return;
         }
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addMember.fxml"));

             Parent parent = loader.load();

             addMemberController controller = (addMemberController) loader.getController();
             controller.inflatedUI(selectedForEdit);
             Stage stage = new  Stage(StageStyle.DECORATED);
             stage.setTitle("Edit Member");
             stage.setScene(new Scene(parent));
             stage.show();

         } catch (IOException ex){
             Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
         }

    }

    @FXML
    void handleRefresh(ActionEvent event) {
    	loadData();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
    private void loadData() {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM addMember";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String memberID = rs.getString("memberID");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone= rs.getString("phone");
                String gender = rs.getString("gender");

                list.add(new Member(memberID, name,email, phone,gender));

            }
        } catch (SQLException ex) {
            Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);
    }
    
  /*  public static class Member {
        private final SimpleStringProperty memberID;
        private final SimpleStringProperty name ;
        private final SimpleStringProperty email;
        private final SimpleStringProperty phone ;
        private final SimpleStringProperty gender ;

        public Member ( String memberID ,  String name , String email , String phone , String gender){
            this.memberID = new SimpleStringProperty(memberID);
            this.name = new SimpleStringProperty(name);
            this.email= new SimpleStringProperty(email);
            this.phone =new SimpleStringProperty(phone);
            this.gender = new SimpleStringProperty(gender);

        }

        public String getMemberID() {
            return memberID.get();
        }

        public String getName() {
            return name.get();
        }

        public String getPhone() {
            return phone.get();
        }

        public String getEmail() {
            return email.get();
        }

        public String getGender() {
            return gender.get();
        }

    }*/
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("https://static.thenounproject.com/png/3314579-200.png"));
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


}
