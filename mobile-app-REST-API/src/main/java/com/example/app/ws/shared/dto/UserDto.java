package com.example.app.ws.shared.dto;

import java.io.Serializable;
import java.util.List;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String titleBeforeName;
    private String titleAfterName;
    private String room;
    private String phoneNumber;
    private String research;
    private Boolean isTeacher;
    private Boolean man;

    private List<NoteDto> notes;
    private List<SubjectDto>subjectList;

    public List<NoteDto> getNotes() {
        return notes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
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

    public List<SubjectDto> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectDto> subjectList) {
        this.subjectList = subjectList;
    }
}
