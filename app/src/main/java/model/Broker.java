package model;

import androidx.annotation.NonNull;

public class Broker {
    private String firstName, lastName, contactNo, emailId, experience, userId;

    public Broker() {
    }

    public Broker(String firstName, String lastName, String contactNo, String emailId, String experience, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.emailId = emailId;
        this.experience = experience;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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
        return "First Name " + firstName +"Last Name " + lastName + "Contact No " + contactNo +"Email Id " + emailId +"Experience " + experience + "User Id : "+ userId;
    }
}
