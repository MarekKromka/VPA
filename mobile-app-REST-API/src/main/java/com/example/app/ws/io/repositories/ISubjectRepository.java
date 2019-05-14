package com.example.app.ws.io.repositories;

import com.example.app.ws.io.entity.SubjectEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

public interface ISubjectRepository extends CrudRepository<SubjectEntity, Long> {
    @Nullable
    SubjectEntity findBySubjectId(@Nullable String subjectId);
}
