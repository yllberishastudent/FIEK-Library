package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
    private final SimpleStringProperty BookID;
    private final SimpleStringProperty Title;
    private final SimpleStringProperty Author;
    private final SimpleStringProperty Publisher ;
    private final SimpleIntegerProperty Quantity ;
    private final SimpleBooleanProperty Availability ;


    public Book ( String BookID ,  String Title , String Author , String Publisher , Integer Quantity, Boolean Availability){
        this.BookID = new SimpleStringProperty(BookID);
        this.Title = new SimpleStringProperty(Title);
        this.Author=new SimpleStringProperty(Author);
        this.Publisher =new SimpleStringProperty(Publisher);
        this.Quantity = new SimpleIntegerProperty(Quantity);
        this.Availability = new SimpleBooleanProperty(Availability);

    }

    public String getBookID() {
        return BookID.get();
    }

    public String getTitle() {
        return Title.get();
    }

    public String getAuthor() {
        return Author.get();
    }

    public String getPublisher() {
        return Publisher.get();
    }

    public Integer getQuantity() {
        return Quantity.get();
    }

    public Boolean getAvailability() {
        return Availability.get();
    }
}