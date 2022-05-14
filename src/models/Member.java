package models;

import javafx.beans.property.SimpleStringProperty;

public class Member {
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

}