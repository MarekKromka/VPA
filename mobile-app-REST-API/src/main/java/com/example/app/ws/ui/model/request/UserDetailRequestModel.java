package com.example.app.ws.ui.model.request;

public class UserDetailRequestModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String titleBeforeName;
    private String titleAfterName;
    private String room;
    private String phoneNumber;
    private String research;
    private String userId;
    private boolean man;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitleBeforeName() {
        return titleBeforeName;
    }

    public void setTitleBeforeName(String titleBeforeName) {
        this.titleBeforeName = titleBeforeName;
    }

    public String getTitleAfterName() {
        return titleAfterName;
    }

    public void setTitleAfterName(String titleAfterName) {
        this.titleAfterName = titleAfterName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }
}
