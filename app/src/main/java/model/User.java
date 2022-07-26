package model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String firstName, lastName, contactNo, emailId, password, userType, userId;
    ArrayList<String> propertiesList = new ArrayList<String>();

    public User() {
    }

    public User(String firstName, String lastName, String contactNo, String emailId, String password, String userType, ArrayList<String> propertiesList, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.emailId = emailId;
        this.password = password;
        this.userType = userType;
        this.propertiesList = propertiesList;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String usertype) {
        this.userType = usertype;
    }

    public ArrayList<String> getPropertiesList() {
        return propertiesList;
    }

    public void setPropertiesList(ArrayList<String> propertiesList) {
        this.propertiesList = propertiesList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name : " + firstName + " " + lastName + " Email Id : " + emailId + " Account Type : " + userType + "My Properties : "+ propertiesList;
    }



}
