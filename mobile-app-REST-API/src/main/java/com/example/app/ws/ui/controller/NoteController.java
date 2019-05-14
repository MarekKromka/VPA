package com.example.app.ws.ui.controller;

import com.example.app.ws.service.INoteService;
import com.example.app.ws.shared.dto.GroupDto;
import com.example.app.ws.shared.dto.NoteDto;
import com.example.app.ws.shared.dto.UserDto;
import com.example.app.ws.ui.model.request.NoteDetailsRequestModel;
import com.example.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("notes") // http://localhost:8080/notes // context path ../mobile-app-ws
public class NoteController {

    @Autowired
    INoteService noteService;

    private ModelMapper mapper = new ModelMapper();

    @GetMapping(
            path = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public NoteRest getNote(@PathVariable String id) {

        NoteDto noteDto = noteService.getNoteByNoteId(id);
        NoteRest returnValue = mapper.map(noteDto, NoteRest.class);
        return returnValue;
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public NoteRest createNote(@RequestBody NoteDetailsRequestModel noteDetails) throws Exception {
        //if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        NoteDto noteDto = mapper.map(noteDetails, NoteDto.class);

        noteDto.setSource(new UserDto());
        noteDto.setTarget(new UserDto());
        if(noteDetails.getTargetGroup() != null){
            noteDto.setTargetGroup(new GroupDto());
            noteDto.getTargetGroup().setGroupId(noteDetails.getTargetGroup());
        }
        noteDto.getSource().setUserId(noteDetails.getSource());
        noteDto.getTarget().setUserId(noteDetails.getTarget());

        if(!noteDetails.getDateOdkedy().isEmpty() && !noteDetails.getDateDokedy().isEmpty()) {
            Date dateTo = new Date();
            dateTo = new SimpleDateFormat("dd/MM/yyyy").parse(noteDetails.getDateOdkedy());
            noteDto.setDateFrom(new SimpleDateFormat("dd/MM/yyyy").parse(noteDetails.getDateOdkedy()));
            noteDto.setDateTo(new SimpleDateFormat("dd/MM/yyyy").parse(noteDetails.getDateDokedy()));
        }
        NoteDto createdNote = noteService.createNote(noteDto);
        NoteRest returnValue = mapper.map(createdNote,NoteRest.class);
        return returnValue;

    }

    @PutMapping(
            path = "/{id}",
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public NoteRest updateNote(@RequestBody NoteDetailsRequestModel noteDetails, @PathVariable String id) {

        //if(noteDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        NoteDto noteDto = mapper.map(noteDetails, NoteDto.class);

        NoteDto updatedNote = noteService.updateNote(id, noteDto);
        return mapper.map(updatedNote, NoteRest.class);

    }

    @DeleteMapping(
            path = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public OperationStatusModel deleteNote(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        noteService.deleteNote(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(
            path = "/myNotes",
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<NoteRest> getAllUserNotes(@RequestParam(value = "myUserId") String userId, @RequestParam(value = "SourceNotes", defaultValue = "false") boolean SourceNotes) {
        List<NoteRest> returnValue = new ArrayList<>();

        List<NoteDto> notes = noteService.getMyNotes(userId, SourceNotes);

        for (NoteDto noteDto : notes) {
            NoteRest noteModel = mapper.map(noteDto, NoteRest.class);
            returnValue.add(noteModel);
        }
        return returnValue;
    }

}
