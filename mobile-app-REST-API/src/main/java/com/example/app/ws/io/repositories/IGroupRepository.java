package com.example.app.ws.io.repositories;

import com.example.app.ws.io.entity.GroupEntity;
import com.example.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IGroupRepository extends CrudRepository<GroupEntity, Long> {
    GroupEntity findByGroupId(String groupId);
    List<GroupEntity> findDistinctByUserListContains(UserEntity user);
}
