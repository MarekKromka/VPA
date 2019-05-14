package com.example.app.ws.service;

import com.example.app.ws.shared.dto.GroupDto;

import java.util.List;

public interface IGroupService {
    GroupDto createGroup(GroupDto group);

    GroupDto addToGroup(String idGroup, String idUser);

    GroupDto getGroupById(String id);

    void deleteGroup(String id);

    GroupDto removeFromGroup(String idGroup, String idUser);

    List<GroupDto> getGroups();
}
