package com.example.app.ws.io.repositories;

import com.example.app.ws.io.entity.GroupEntity;
import com.example.app.ws.io.entity.NoteEntity;
import com.example.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface INoteRepository extends CrudRepository<NoteEntity, Long> {
    NoteEntity findByNoteId(String noteId);
    Iterable<NoteEntity> findAllByTarget(UserEntity target);
    Iterable<NoteEntity> findAllByTargetGroup(GroupEntity group);
}
