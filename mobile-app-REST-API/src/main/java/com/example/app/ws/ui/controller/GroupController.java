package com.example.app.ws.ui.controller;

import com.example.app.ws.exceptions.GroupServiceException;
import com.example.app.ws.io.entity.GroupEntity;
import com.example.app.ws.service.IGroupService;
import com.example.app.ws.shared.dto.GroupDto;
import com.example.app.ws.ui.model.request.GroupDetailRequestModel;
import com.example.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("groups") // http://localhost:8080/groups // context path ../mobile-app-ws
public class GroupController {

    @Autowired
    private IGroupService groupService;

    private ModelMapper mapper = new ModelMapper();

    @GetMapping(
            path = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    public GroupRest getGroup(@PathVariable String id) {

        GroupDto groupDto = groupService.getGroupById(id);
        GroupRest returnValue = mapper.map(groupDto, GroupRest.class);

        return returnValue;

    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    public GroupRest createGroup(@RequestBody GroupDetailRequestModel groupDetails) throws Exception {
        GroupDto groupDto = mapper.map(groupDetails, GroupDto.class);

        if(groupDetails.getName().isEmpty()) throw new GroupServiceException(String.format(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage(),"name"));

        GroupDto createdNote = groupService.createGroup(groupDto);
        GroupRest returnValue = mapper.map(createdNote, GroupRest.class);
        return returnValue;
    }

    @PutMapping(
            path = "/{idGroup}/users/{idUser}",
            produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    public GroupRest addUserToGroup(@PathVariable(value = "idGroup") String idGroup, @PathVariable(value = "idUser") String idUser) {

        GroupDto updatedGroup = groupService.addToGroup(idGroup, idUser);

        GroupRest returnValue = mapper.map(updatedGroup, GroupRest.class);
        return returnValue;
    }

    @DeleteMapping(
            path = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteGroup(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel(RequestObjectName.GROUP.name(),
                RequestOperationName.DELETE.name());

        groupService.deleteGroup(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @DeleteMapping(
            path = "/{idGroup}/users/{idUser}",
            produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    public GroupRest removeUserFromGroup(@PathVariable(value = "idGroup") String idGroup, @PathVariable(value = "idUser") String idUser) {

        GroupDto updatedGroup = groupService.removeFromGroup(idGroup, idUser);

        GroupRest returnValue = mapper.map(updatedGroup, GroupRest.class);
        return returnValue;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public List<GroupRest> getAllGroups() {
        List<GroupRest> returnValue = new ArrayList<>();

        List<GroupDto> groups = groupService.getGroups();

        for (GroupDto groupDto : groups) {
            GroupRest groupModel = mapper.map(groupDto, GroupRest.class);
            returnValue.add(groupModel);
        }
        return returnValue;

    }

}
