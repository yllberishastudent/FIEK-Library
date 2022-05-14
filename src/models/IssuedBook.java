package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class IssuedBook{
    private final SimpleStringProperty bookId;
    private final SimpleStringProperty memberId;
    private final SimpleIntegerProperty renew;
    private final SimpleStringProperty TimeCol;

    public IssuedBook(String bookId, String memberId, String TimeCol,Integer renew) {
        this.bookId = new SimpleStringProperty(bookId);
        this.memberId = new SimpleStringProperty(memberId);
        this.TimeCol = new SimpleStringProperty(TimeCol);
        this.renew = new SimpleIntegerProperty(renew);

    }
    public String getBookId() {
        return bookId.get();
    }

    public String getMemberId() {
        return memberId.get();
    }

    public Integer getRenew() {
        return renew.get();
    }

    public String getIssueTime() {
        return TimeCol.get();
    }      
}