package com.example.app.ws.ui.model.response;

import java.util.List;

public class SubjectRest {
    private String subjectId;
    private String name;
    private String informations;
    private List<UserRest2> usersList;

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

    public List<UserRest2> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserRest2> usersList) {
        this.usersList = usersList;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
