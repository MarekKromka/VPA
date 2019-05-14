package com.example.app.ws.io.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "subjects")
public class SubjectEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String subjectId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String informations;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_subject",
            joinColumns = @JoinColumn(name = "subject_Id"),
            inverseJoinColumns = @JoinColumn(name = "user_Id"))
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
