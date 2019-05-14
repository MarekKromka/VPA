package com.example.app.ws.service.impl;

import com.example.app.ws.exceptions.SubjectServiceException;
import com.example.app.ws.io.entity.SubjectEntity;
import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.io.repositories.ISubjectRepository;
import com.example.app.ws.io.repositories.IUserRepository;
import com.example.app.ws.service.ISubjectService;
import com.example.app.ws.shared.Utils;
import com.example.app.ws.shared.dto.SubjectDto;
import com.example.app.ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements ISubjectService {

    ModelMapper mapper = new ModelMapper();

    @Autowired
    Utils utils;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    ISubjectRepository subjectRepository;

    @Override
    public SubjectDto createSubject(SubjectDto subject) {
        SubjectEntity subjectEntity = mapper.map(subject, SubjectEntity.class);

        String publicSubjectId = utils.generateUserId(30);
        subjectEntity.setSubjectId(publicSubjectId);

        SubjectEntity storedSubjectDetails = subjectRepository.save(subjectEntity);

        SubjectDto returnValue = mapper.map(storedSubjectDetails, SubjectDto.class);
        return returnValue;
    }

    @Override
    public SubjectDto addToSubject(String idSubject, String idUser) {
        SubjectEntity subject = subjectRepository.findBySubjectId(idSubject);
        if(subject == null) throw new SubjectServiceException(idSubject);
        UserEntity user = userRepository.findByUserId(idUser);
        if(user == null) throw new EntityNotFoundException(idUser);

        if(subject.getUsersList().contains(user)) throw new SubjectServiceException("User already has this subject") ;
        subject.getUsersList().add(user);
        SubjectEntity updatedSubject = subjectRepository.save(subject);

        SubjectDto returnValue = new ModelMapper().map(updatedSubject, SubjectDto.class);
        return returnValue;
    }

    @Override
    public void deleteSubject(String id) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectId(id);

        if(subjectEntity == null) throw new EntityNotFoundException(id);

        subjectRepository.delete(subjectEntity);
    }

    @Override
    public SubjectDto removeFromSubject(String idSubject, String idUser) {
        SubjectEntity subject = subjectRepository.findBySubjectId(idSubject);
        if(subject == null) throw new EntityNotFoundException(idSubject);

        UserEntity user = userRepository.findByUserId(idUser);
        if(user == null) throw new EntityNotFoundException(idUser);

        subject.getUsersList().remove(user);
        SubjectEntity updatedSubject = subjectRepository.save(subject);

        SubjectDto returnValue = new ModelMapper().map(updatedSubject, SubjectDto.class);
        return returnValue;
    }

    @Override
    public List<SubjectDto> getSubjects() {
        List<SubjectDto> returnValue = new ArrayList<>();

        List<SubjectEntity> subjects = new ArrayList<>();
        Iterable<SubjectEntity> subjectEntityIterable = subjectRepository.findAll();

        if(subjectEntityIterable == null) throw new SubjectServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        subjectEntityIterable.forEach(subjects::add);

        for (SubjectEntity subject : subjects) {
            SubjectDto subjectDto = mapper.map(subject, SubjectDto.class);
            returnValue.add(subjectDto);
        }
        return returnValue;
    }


    @Override
    public SubjectDto getSubjectById(String id) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectId(id);
        if(subjectEntity == null) throw new SubjectServiceException(String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(),id));

        SubjectDto returnValue = mapper.map(subjectEntity,SubjectDto.class);
        return returnValue;
    }
}
