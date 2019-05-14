package com.example.app.ws.service.impl;

import com.example.app.ws.exceptions.GroupServiceException;
import com.example.app.ws.io.entity.GroupEntity;
import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.io.repositories.IGroupRepository;
import com.example.app.ws.io.repositories.IUserRepository;
import com.example.app.ws.service.IGroupService;
import com.example.app.ws.shared.Utils;
import com.example.app.ws.shared.dto.GroupDto;
import com.example.app.ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements IGroupService {

    ModelMapper mapper = new ModelMapper();

    @Autowired
    Utils utils;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IGroupRepository groupRepository;

    @Override
    public GroupDto createGroup(GroupDto group) {
        GroupEntity groupEntity = mapper.map(group, GroupEntity.class);

        String publicGroupId = utils.generateUserId(30);
        groupEntity.setGroupId(publicGroupId);

        GroupEntity storedGroupDetails = groupRepository.save(groupEntity);

        GroupDto returnValue = mapper.map(storedGroupDetails, GroupDto.class);
        return returnValue;
    }

    @Override
    public GroupDto addToGroup(String idGroup, String idUser) {
        GroupEntity group = groupRepository.findByGroupId(idGroup);
        if(group == null) throw new EntityNotFoundException(idGroup);
        UserEntity user = userRepository.findByUserId(idUser);
        if(user == null) throw new EntityNotFoundException(idUser);

        if(group.getUserList().contains(user)) throw new GroupServiceException("User is already in this group.");
        group.getUserList().add(user);
        GroupEntity updatedGroup = groupRepository.save(group);

        GroupDto returnValue = new ModelMapper().map(updatedGroup, GroupDto.class);
        return returnValue;
    }

    @Override
    public GroupDto getGroupById(String id) {

        GroupEntity groupEntity = groupRepository.findByGroupId(id);
        if(groupEntity == null) throw new GroupServiceException(String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(),id));

        GroupDto returnValue = mapper.map(groupEntity,GroupDto.class);
        return returnValue;
    }

    @Override
    public void deleteGroup(String id) {
        GroupEntity groupEntity = groupRepository.findByGroupId(id);

        if(groupEntity == null) throw new EntityNotFoundException(id);

        groupRepository.delete(groupEntity);
    }

    @Override
    public GroupDto removeFromGroup(String idGroup, String idUser) {
        GroupEntity group = groupRepository.findByGroupId(idGroup);
        if(group == null) throw new EntityNotFoundException(idGroup);

        UserEntity user = userRepository.findByUserId(idUser);
        if(user == null) throw new EntityNotFoundException(idUser);
        group.getUserList().remove(user);
        GroupEntity updatedGroup = groupRepository.save(group);

        GroupDto returnValue = new ModelMapper().map(updatedGroup, GroupDto.class);
        return returnValue;
    }

    @Override
    public List<GroupDto> getGroups() {
        List<GroupDto> returnValue = new ArrayList<>();

        List<GroupEntity> groups = new ArrayList<>();
        Iterable<GroupEntity> groupEntityIterable = groupRepository.findAll();

        if(groupEntityIterable == null) throw new GroupServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        groupEntityIterable.forEach(groups::add);

        for (GroupEntity group : groups) {
            GroupDto groupDto = mapper.map(group, GroupDto.class);
            returnValue.add(groupDto);
        }
        return returnValue;
    }
}
