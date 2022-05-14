package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Issue {
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
}