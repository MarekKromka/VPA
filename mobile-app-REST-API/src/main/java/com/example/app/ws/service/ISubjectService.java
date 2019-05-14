package com.example.app.ws.service;

import com.example.app.ws.shared.dto.SubjectDto;

import java.util.List;

public interface ISubjectService {
    SubjectDto getSubjectById(String id);

    SubjectDto createSubject(SubjectDto subjectDto);

    SubjectDto addToSubject(String idSubject, String idUser);

    void deleteSubject(String id);

    SubjectDto removeFromSubject(String idSubject, String idUser);

    List<SubjectDto> getSubjects();
}
