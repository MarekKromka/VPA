package com.example.app.ws.ui.model.response;

public class UserRest2 {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String titleBeforeName;
    private String titleAfterName;
    private String room;
    private String phoneNumber;
    private String research;
    private Boolean isTeacher;
    private Boolean man;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getTeacher() {
        return isTeacher;
    }

    public void setTeacher(Boolean teacher) {
        isTeacher = teacher;
    }

    public Boolean getMan() {
        return man;
    }

    public void setMan(Boolean man) {
        this.man = man;
    }
}
