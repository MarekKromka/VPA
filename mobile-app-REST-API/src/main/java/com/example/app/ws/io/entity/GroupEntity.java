package com.example.app.ws.io.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "groups_of_users")
public class GroupEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String groupId;
    @Column(nullable = false, length = 50)
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_group",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> userList;

    @OneToMany(mappedBy = "targetGroup", cascade = CascadeType.ALL)
    private List<NoteEntity> notesForGroup;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }

    public List<NoteEntity> getNotesForGroup() {
        return notesForGroup;
    }

    public void setNotesForGroup(List<NoteEntity> notesForGroup) {
        this.notesForGroup = notesForGroup;
    }
}
