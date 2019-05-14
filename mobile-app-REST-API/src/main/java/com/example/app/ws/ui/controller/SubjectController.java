package com.example.app.ws.ui.controller;

import com.example.app.ws.exceptions.SubjectServiceException;
import com.example.app.ws.service.ISubjectService;
import com.example.app.ws.shared.dto.SubjectDto;
import com.example.app.ws.ui.model.request.SubjectDetailRequestModel;
import com.example.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("subjects") // http://localhost:8080/subjects // context path ../mobile-app-ws
public class SubjectController {

    @Autowired
    private ISubjectService subjectService;

    private ModelMapper mapper = new ModelMapper();

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public SubjectRest getSubject(@PathVariable String id) {

        SubjectDto groupDto = subjectService.getSubjectById(id);
        SubjectRest returnValue = mapper.map(groupDto, SubjectRest.class);

        return returnValue;

    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public SubjectRest createSubject(@RequestBody SubjectDetailRequestModel subjectDetails) throws Exception {
        SubjectDto subjectDto = mapper.map(subjectDetails, SubjectDto.class);

        if(subjectDetails.getName().isEmpty()) throw new SubjectServiceException(String.format(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage(),"name"));

        SubjectDto createdNote = subjectService.createSubject(subjectDto);
        SubjectRest returnValue = mapper.map(createdNote, SubjectRest.class);
        return returnValue;
    }

    @PutMapping(
            path = "/{idSubject}/users/{idUser}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public SubjectRest addUserToSubject(@PathVariable(value = "idSubject") String idSubject, @PathVariable(value = "idUser") String idUser) {

        SubjectDto updatedSubject = subjectService.addToSubject(idSubject, idUser);

        SubjectRest returnValue = mapper.map(updatedSubject, SubjectRest.class);
        return returnValue;
    }

    @DeleteMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteSubject(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel(RequestObjectName.GROUP.name(),
                RequestOperationName.DELETE.name());

        subjectService.deleteSubject(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @DeleteMapping(
            path = "/{idSubject}/users/{idUser}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public SubjectRest removeUserFromSubject(@PathVariable(value = "idSubject") String idSubject, @PathVariable(value = "idUser") String idUser) {

        SubjectDto updatedSubject = subjectService.removeFromSubject(idSubject, idUser);

        SubjectRest returnValue = mapper.map(updatedSubject, SubjectRest.class);
        return returnValue;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public List<SubjectRest> getAllSubjects() {
        List<SubjectRest> returnValue = new ArrayList<>();

        List<SubjectDto> subjects = subjectService.getSubjects();

        for (SubjectDto subject : subjects) {
            SubjectRest subjectRest = mapper.map(subject, SubjectRest.class);
            returnValue.add(subjectRest);
        }
        return returnValue;

    }

}
