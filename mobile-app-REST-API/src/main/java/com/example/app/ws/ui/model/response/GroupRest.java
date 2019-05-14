package com.example.app.ws.ui.model.response;

import java.util.List;

public class GroupRest {
    private String groupId;
    private String name;
    private List<UserRest> userList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserRest> getUserList() {
        return userList;
    }

    public void setUserList(List<UserRest> userList) {
        this.userList = userList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
