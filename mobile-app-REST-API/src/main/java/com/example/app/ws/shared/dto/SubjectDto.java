package com.example.app.ws.shared.dto;

import com.example.app.ws.io.entity.UserEntity;

import java.util.List;

public class SubjectDto {
    private long id;
    private String subjectId;
    private String name;
    private String informations;
    private List<UserEntity> usersList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformations() {
        return informations;
    }

    public void setInformations(String informations) {
        this.informations = informations;
    }

    public List<UserEntity> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserEntity> usersList) {
        this.usersList = usersList;
    }
}
