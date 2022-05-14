package controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Issue;
import repositories.DatabaseHandler;
import controllers.MainController;

public class MainController implements Initializable {
	
	ObservableList<Issue> issueData = FXCollections.observableArrayList();
	 DatabaseHandler databaseHandler;
	 boolean isReadyForSubmission = false;

	 PieChart bookChart;
	 PieChart memberChart;
	 
    @FXML
    private Tab BookIssueTab;

    @FXML
    private Tab BookIssueTab1;

    @FXML
    private TableColumn<Issue, String> authorColumn;

    @FXML
    private Text authorName;

    @FXML
    private Text availability;

    @FXML
    private TableColumn<Issue, String> bidColumn;

    @FXML
    private TextField bookID;

    @FXML
    private TextField bookIdInput;

    @FXML
    private StackPane bookInfoContainer;

    @FXML
    private Text bookName;

    @FXML
    private HBox book_info;

    @FXML
    private HBox book_info1;

    @FXML
    private Text contact;

    @FXML
    private TableColumn<Issue, String> emailColumn;

    @FXML
    private TableColumn<Issue, String> idColumn;

    @FXML
    private TableColumn<Issue, String> issueDateColumn;

    @FXML
    private TextField memberID;

    @FXML
    private TextField memberIdInput;

    @FXML
    private StackPane memberInfoContainer;

    @FXML
    private Text memberName;

    @FXML
    private HBox member_info;

    @FXML
    private TableColumn<Issue, String> nameColumn;

    @FXML
    private TableColumn<Issue, String> renewColumn;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Issue> tableView;

    @FXML
    private TableColumn<Issue, String> titleColumn;

    @FXML
    void aboutHandler(ActionEvent event) {

    }

    @FXML
    void handleAddBook(ActionEvent event) {
    	   loadWindow("/views/addBook.fxml", "Add Book");
    }

    @FXML
    void handleAddMember(ActionEvent event) {
    	 loadWindow("/views/viewMembers.fxml", "View Members");
    }

    @FXML
    void handleMenuClose(ActionEvent event) {
    	 ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    void handleMenuLogOut(ActionEvent event) {  
    	Stage stage = (Stage) member_info.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void handleViewBooks(ActionEvent event) {
    	 loadWindow("/views/booklist.fxml", "View Books");
    }

    @FXML
    void handleViewIssuedBooks(ActionEvent event) {
    	 loadWindow("/views/issuedBooks.fxml", "View Issued Books");
    }

    @FXML
    void handleViewMembers(ActionEvent event) {
    	loadWindow("/views/viewMembers.fxml", "View Members");
    }

    @FXML
    void issueHandler(ActionEvent actionEvent) throws SQLException {String memberID = memberIdInput.getText();
    String bookID = bookIdInput.getText();
    String isAvailable = availability.getText();

    if (memberID.isEmpty() || bookID.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("You must select Book ID and Member ID to issue a book!");
        alert.showAndWait();
    } else {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book titled " + bookName.getText() + " to " + memberName.getText() + " ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String query = "SELECT memberID,bookID from issuedBooks where memberID='" + memberID + "'" +
                    "and bookID ='" + bookID + "'";
            ResultSet result = databaseHandler.execQuery(query);

            try {
                if (result.next()) {
                    Alert bookIsAlreadyIssuedAlert = new Alert(Alert.AlertType.ERROR);
                    bookIsAlreadyIssuedAlert.setTitle("Duplicate Entry");
                    bookIsAlreadyIssuedAlert.setHeaderText(null);
                    bookIsAlreadyIssuedAlert.setContentText("This book was once issued to this member. You can renew it!");
                    bookIsAlreadyIssuedAlert.showAndWait();
                    clearIssueEntries();
                    hideShowGraph(true);
                    return;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (isAvailable == "Available") {
                String query1 = "INSERT INTO issuedBooks(bookID,memberID) VALUES("
                        + "'" + bookID + "',"
                        + "'" + memberID + "')";
                

                String query2 = "UPDATE addBook SET quantity=quantity-1  where bookid='" + bookID + "'";


                if (databaseHandler.execAction(query1) && databaseHandler.execAction(query2)) {
                    refreshGraphs();
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("SUCCESS");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book was issued successfully!");
                    alert1.showAndWait();
                    clearIssueEntries();
                    hideShowGraph(true);

                    String query3 = "SELECT quantity from addBook where Bookid='" + bookID + "'";
                    ResultSet rs = databaseHandler.execQuery(query3);
                    if (rs.next()) {
                        int qty = rs.getInt("quantity");
                        if (qty == 0) {
                            String query4 = "UPDATE addBook SET  isAvail=false  where Bookid='" + bookID + "'";
                            databaseHandler.execAction(query4);
                        }
                    }

                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("FAILED");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Sorry, we couldn't issue the book!");
                    alert2.showAndWait();
                    clearIssueEntries();
                    hideShowGraph(true);
                }
            } else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("FAILED");
                alert3.setHeaderText(null);
                alert3.setContentText("This book isn't available!");
                alert3.showAndWait();
                clearIssueEntries();
                hideShowGraph(true);
            }
        } else if (response.get() == ButtonType.CANCEL) {
            Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
            alert4.setTitle("CANCELED");
            alert4.setHeaderText(null);
            alert4.setContentText("Book issue was canceled!");
            alert4.showAndWait();
            clearIssueEntries();
            hideShowGraph(true);
        }
    }
}

    @FXML
    void loadBookInfo(ActionEvent event) {  
    
    	clearBookcache();
    	String id = bookIdInput.getText();
    	String query = "SELECT * FROM addBook WHERE Bookid='" + id + "'";
    	ResultSet rs = databaseHandler.execQuery(query);
    	Boolean flag = false;

    try {
        while (rs.next()) {
            hideShowGraph(false);
            String bName = rs.getString("title");
            String bAuthor = rs.getString("author");
            Integer bStatus = rs.getInt("quantity");
            bookName.setVisible(true);
            bookName.setText(bName);
            authorName.setVisible(true);
            authorName.setText(bAuthor);
            String status = (bStatus) > 0 ? "Available" : "Not Available";
            availability.setVisible(true);
            availability.setText(status);
            flag = true;
        }
        if (flag == false) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book not found");
            alert.setHeaderText(null);
            alert.setContentText("No such book with this ID is found!");
            alert.showAndWait();
            bookIdInput.clear();

        }
    } catch (SQLException ex) {
        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @FXML
    void loadBookInfo2(ActionEvent event) { 
    	issueData.clear();
    DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

    String id = bookID.getText();
    String mid = memberID.getText();
    System.out.println("yooo firstthe ");
    if (!id.isEmpty() && !mid.isEmpty()) {

        String qu = "SELECT * FROM issuedBooks WHERE bookID = '" + id + "' and memberID='" + mid + "'";

        System.out.println(qu);
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            if (!rs.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("The book with ID: " + id + " wasn't issued to member with  ID: " + mid + " !");
                alert.showAndWait();
                clearOnSubmissionIssueEntries();
                return;
            }
        //   issueData.add(new Issue("issueTime", 2, "mBookID", "title", "author", "mMemberID", "name", "email"));
        //    rs.beforeFirst();
        //  rs.next();
        // System.out.println(rs.next());
        //  boolean tempp = rs.next();
       //  System.out.println(tempp);
            
            ResultSet rst = databaseHandler.execQuery(qu);
          
            while (rst.next()) {
            	
System.out.println("Eshte duke punuar");
                String mBookID = id;
                String mMemberID = mid;
                Timestamp mIssueTime = rs.getTimestamp("issueTime");
                String issueTime = mIssueTime.toString();
                int mRenewCount = rs.getInt("renew_count");
               
                String query = "SELECT * FROM addBook WHERE bookid = '" + id + "'";
                ResultSet r1 = databaseHandler.execQuery(query);

                String qu1 = "SELECT * FROM addMember WHERE memberID = '" + mid + "'";
                ResultSet r2 = databaseHandler.execQuery(qu1);
                
      
                while (r1.next() && r2.next()) {

                    String title = r1.getString("title");
                    String author = r1.getString("author");
                    String name = r2.getString("name");
                    String email = r2.getString("email");

                    issueData.add(new Issue(issueTime, mRenewCount, mBookID, title, author, mMemberID, name, email));

                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        isReadyForSubmission = true;
        tableView.setItems(issueData);

    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("You must select Book ID and Member ID to view the information!");
        alert.showAndWait();
        clearOnSubmissionIssueEntries();
    }
}
    @FXML
    void loadMemberInfo(ActionEvent event) {
    	clearMembercache();


        String id = memberIdInput.getText();
        String query = "SELECT * FROM addMember WHERE memberID='" + id + "'";
        ResultSet rs = databaseHandler.execQuery(query);
        Boolean flag = false;
        try {
            while (rs.next()) {
                hideShowGraph(false);
                String mName = rs.getString("name");
                String mMobile = rs.getString("email");

                memberName.setVisible(true);
                memberName.setText(mName);
                contact.setVisible(true);
                contact.setText(mMobile);

                flag = true;
            }
            if (flag == false) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Member not found");
                alert.setHeaderText(null);
                alert.setContentText("No such member with this ID is found!");
                alert.showAndWait();
                memberIdInput.clear();
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void loadOnSubmissionOp(ActionEvent event) { if (isReadyForSubmission == false) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed");
        alert.setHeaderText(null);
        alert.setContentText("Please Select A Book To Submit!");
        alert.showAndWait();
        return;
    }
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Submission Operation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to return the book ?");
    Optional<ButtonType> response = alert.showAndWait();
    if (response.get() == ButtonType.OK) {

        String id = bookID.getText();
        String mid = memberID.getText();
        String ac1 = " DELETE FROM issuedBooks WHERE bookID = '" + id + "' and memberID = '" + mid + "'";
        String ac2 = "UPDATE addBook SET quantity = quantity+1  WHERE Bookid= '" + id + "'";


        if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
            Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
            confirmAlert.setTitle("Success");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Book Has Been Submitted!");
            confirmAlert.showAndWait();
            clearOnSubmissionIssueEntries();
            isReadyForSubmission = false;
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Submission Has Been Failed!");
            alert1.showAndWait();
            clearOnSubmissionIssueEntries();
            isReadyForSubmission = false;
        }

    } else {
        Alert canceledAlert = new Alert(Alert.AlertType.INFORMATION);
        canceledAlert.setTitle("Canceled");
        canceledAlert.setHeaderText(null);
        canceledAlert.setContentText("Submission Operation canceled!");
        canceledAlert.showAndWait();
        isReadyForSubmission = false;
        clearOnSubmissionIssueEntries();
    }
}

    @FXML
    void loadRenewOp(ActionEvent event) { if (!isReadyForSubmission) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed");
        alert.setHeaderText(null);
        alert.setContentText("Please Select A Book To Renew!");
        alert.showAndWait();
        return;
    }

    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
    alert1.setTitle("Confirm Renew Operation");
    alert1.setHeaderText(null);
    alert1.setContentText("Are you sure you want to renew the book ?");
    Optional<ButtonType> response = alert1.showAndWait();
    if (response.get() == ButtonType.OK) {
        String ac = "UPDATE issuedBooks SET issueTime = CURRENT_TIMESTAMP,renew_count = renew_count+1 WHERE bookID = '" + bookID.getText() + "'";
        System.out.println(ac);
        if (databaseHandler.execAction(ac)) {

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Success");
            alert2.setHeaderText(null);
            alert2.setContentText("Book has been renewed!");
            alert2.showAndWait();
            clearOnSubmissionIssueEntries();
            isReadyForSubmission = false;

        } else {
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("Failed");
            alert3.setHeaderText(null);
            alert3.setContentText("Renew Has Been Failed!");
            alert3.showAndWait();
            clearOnSubmissionIssueEntries();
            isReadyForSubmission = false;
        }
    } else {
        Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
        alert4.setTitle("Canceled");
        alert4.setHeaderText(null);
        alert4.setContentText("Renew Operation canceled!");
        alert4.showAndWait();
        clearOnSubmissionIssueEntries();
        isReadyForSubmission = false;
    }
}

    @FXML
    void logoutAction(ActionEvent event) {
    	Stage stage = (Stage) member_info.getScene().getWindow();
    	stage.close();
    	 loadWindow("/views/login.fxml", "Add Member");
    }

   

    @FXML
    void refreshGraphs(ActionEvent event) {
     refreshGraphs();
     System.out.println("Refreshed");
     clearMembercache();
     clearIssueEntries();
     hideShowGraph(true);
    }
    
    
    
    // Loading the corresponding windows when buttons are clicked
    @FXML
    private void loadAddMember(javafx.event.ActionEvent actionEvent) {
        loadWindow("/views/addMember.fxml", "Add Member");
    }

    @FXML
    private void loadAddBook(javafx.event.ActionEvent actionEvent) {
        loadWindow("/views/addBook.fxml", "Add Book");
    }

    @FXML
    private void loadViewMembers(javafx.event.ActionEvent actionEvent) {
        loadWindow("/views/viewMembers.fxml", "Members List");
    }

    @FXML
    private void loadViewBooks(javafx.event.ActionEvent actionEvent) {
        loadWindow("/views/booklist.fxml", "Book list");
    }

    @FXML
    private void loadViewIssuedBooks(javafx.event.ActionEvent actionEvent) {
        loadWindow("/views/issuedBooks.fxml", "Issued Books");
    }

    // A method for loading windows
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

    // Issue Class
 /*   public static class Issue {
        private final SimpleStringProperty memberID;
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty bookID;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty issueTime;
        private final SimpleIntegerProperty renew_count;


        public Issue(String issueTime, Integer renew_count, String bookID, String title, String author, String memberID, String name, String email) {
            this.memberID = new SimpleStringProperty(memberID);
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.bookID = new SimpleStringProperty(bookID);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.issueTime = new SimpleStringProperty(issueTime);
            this.renew_count = new SimpleIntegerProperty(renew_count);
        }

        public String getMemberID() {
            return memberID.get();
        }

        public String getName() {
            return name.get();
        }

        public String getEmail() {
            return email.get();
        }

        public String getBookID() {
            return bookID.get();
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getIssueTime() {
            return issueTime.get();
        }

        public Integer getRenew_count() {
            return renew_count.get();
        }
    } */
    private void clearIssueEntries() {
        bookIdInput.clear();
        memberIdInput.clear();
        clearBookcache();
        clearMembercache();
    }

    // Clearing the info
    private void clearOnSubmissionIssueEntries() {
        bookID.clear();
        memberID.clear();
    }
    // If exceptions happen during the loading process of web pages
    private void handleWebpageLoadException(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(new StackPane(browser));
        stage.setScene(scene);
        stage.show();
    }
    private void loadWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
            handleWebpageLoadException(url);
        }
    }
    // Clearing the member cache
    void clearMembercache() {
        memberName.setText("");
        contact.setText("");

    }
    // A method to clear the book cache when we search for a book
    void clearBookcache() {
        bookName.setText("");
        authorName.setText("");
        availability.setText("");
    }
    // Hidding/Showing the graphs
    private void hideShowGraph(Boolean status) {
        if (status) {
            bookChart.setOpacity(1);
            memberChart.setOpacity(1);
        } else {
            bookChart.setOpacity(0);
            memberChart.setOpacity(0);
        }
    }
    public void initialize(URL rl, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        initGraph();
        initCol();
    }
    private void initCol() {
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueTime"));
        renewColumn.setCellValueFactory(new PropertyValueFactory<>("renew_count"));
        bidColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    // Showing the graphs
    private void initGraph() {

        bookChart = new PieChart(databaseHandler.getBookGraphicStatistics());
        memberChart = new PieChart(databaseHandler.getMemberGraphicStatistics());
        bookInfoContainer.getChildren().add(bookChart);
        memberInfoContainer.getChildren().add(memberChart);
    }
    // Refreshing the graphs
    public void refreshGraphs() {
        bookChart.setData(databaseHandler.getBookGraphicStatistics());
        memberChart.setData(databaseHandler.getMemberGraphicStatistics());
    }
    

}
