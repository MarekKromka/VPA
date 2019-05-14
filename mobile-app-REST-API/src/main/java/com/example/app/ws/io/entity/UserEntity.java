package com.example.app.ws.io.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 120)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(nullable = false)
    private Boolean isTeacher = false;
    @Column(length = 20)
    private String titleBeforeName;
    @Column(length = 20)
    private String titleAfterName;
    @Column(length = 20)
    private String room;
    @Column(length = 20)
    private String phoneNumber;
    @Column(columnDefinition = "TEXT")
    private String research;
    @Column()
    private Boolean man;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
    private List<NoteEntity> notesTarget;
    @OneToMany(mappedBy = "source")
    private List<NoteEntity> notesSource;

    @ManyToMany(mappedBy = "userList")
    private List<GroupEntity> groupList;

    @ManyToMany(mappedBy = "usersList")
    private List<SubjectEntity> subjectList;

    public List<GroupEntity> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupEntity> groupList) {
        this.groupList = groupList;
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

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public List<NoteEntity> getNotesTarget() {
        return notesTarget;
    }

    public void setNotesTarget(List<NoteEntity> notesTarget) {
        this.notesTarget = notesTarget;
    }

    public List<NoteEntity> getNotesSource() {
        return notesSource;
    }

    public void setNotesSource(List<NoteEntity> notesSource) {
        this.notesSource = notesSource;
    }

    public Boolean getTeacher() {
        return isTeacher;
    }

    public void setTeacher(Boolean teacher) {
        isTeacher = teacher;
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

    public Boolean getMan() {
        return man;
    }

    public void setMan(Boolean man) {
        this.man = man;
    }

    public List<SubjectEntity> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectEntity> subjectList) {
        this.subjectList = subjectList;
    }
}
