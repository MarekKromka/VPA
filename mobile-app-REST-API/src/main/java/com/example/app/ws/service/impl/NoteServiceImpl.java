package com.example.app.ws.service.impl;

import com.example.app.ws.exceptions.NoteServiceException;
import com.example.app.ws.io.entity.GroupEntity;
import com.example.app.ws.io.entity.NoteEntity;
import com.example.app.ws.io.repositories.IGroupRepository;
import com.example.app.ws.io.repositories.INoteRepository;
import com.example.app.ws.io.repositories.IUserRepository;
import com.example.app.ws.service.INoteService;
import com.example.app.ws.shared.Utils;
import com.example.app.ws.shared.dto.NoteDto;
import com.example.app.ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoteServiceImpl implements INoteService {

    @Autowired
    INoteRepository noteRepository;
    @Autowired
    Utils utils;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IGroupRepository groupRepository;

    private ModelMapper mapper = new ModelMapper();
    @Override
    public NoteDto createNote(NoteDto note) {

        ModelMapper mapper = new ModelMapper();
        NoteEntity noteEntity = mapper.map(note, NoteEntity.class);

        String publicNoteId = utils.generateUserId(30);
        noteEntity.setNoteId(publicNoteId);
        Date date = new Date();
        noteEntity.setDateCreated(new Timestamp(date.getTime()));
        noteEntity.setSource(userRepository.findByUserId(note.getSource().getUserId()));
        noteEntity.setTarget(userRepository.findByUserId(note.getTarget().getUserId()));
        noteEntity.setTargetGroup(groupRepository.findByGroupId(note.getTargetGroup().getGroupId()));

        NoteEntity storedNoteDetails = noteRepository.save(noteEntity);

        NoteDto returnValue = mapper.map(storedNoteDetails, NoteDto.class);
        return returnValue;
    }

    @Override
    public NoteDto getNoteByNoteId(String noteId) {

        NoteEntity noteEntity = noteRepository.findByNoteId(noteId);

        if(noteEntity == null) throw new NoteServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());


        NoteDto returnValue = mapper.map(noteEntity, NoteDto.class);
        return returnValue;
    }

    @Override
    public NoteDto updateNote(String noteId, NoteDto note) {

        NoteEntity noteEntity = noteRepository.findByNoteId(noteId);

        if(noteEntity == null) throw new NoteServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        if(!note.getHeader().isEmpty()) noteEntity.setHeader(note.getHeader());
        if(!note.getText().isEmpty()) noteEntity.setText(note.getText());
        if(note.getPriority() != 0) noteEntity.setPriority(note.getPriority());
        if(note.getDateFrom() != null) noteEntity.setDateFrom(note.getDateFrom());
        if(note.getDateTo() != null) noteEntity.setDateTo(note.getDateTo());

        NoteEntity updatedNote = noteRepository.save(noteEntity);

        return mapper.map(updatedNote, NoteDto.class);
    }

    @Override
    public void deleteNote(String noteId) {
        NoteEntity noteEntity = noteRepository.findByNoteId(noteId);

        if(noteEntity == null) throw new NoteServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        noteRepository.delete(noteEntity);
    }

    @Override
    public List<NoteDto> getMyNotes(String userId, boolean SourceNotes) {
        List<NoteDto> returnValue = new ArrayList<>();

        List<NoteEntity> notes = new ArrayList<>();
        //find all notes for target only
        Iterable<NoteEntity> notesIterable = noteRepository.findAllByTarget(userRepository.findByUserId(userId));

        if(notesIterable == null) throw new NoteServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        notesIterable.forEach(notes::add);

        //find all notes for groups target belongs to
        List<GroupEntity> groups = groupRepository.findDistinctByUserListContains(userRepository.findByUserId(userId));

        for ( GroupEntity group: groups ) {
            Iterable<NoteEntity> notesIte = noteRepository.findAllByTargetGroup(group);
            notesIte.forEach(notes::add);
        }

        for (NoteEntity note : notes) {
            NoteDto noteDto = mapper.map(note, NoteDto.class);
            returnValue.add(noteDto);
        }

        return returnValue;
    }
}
