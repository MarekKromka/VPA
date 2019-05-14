package com.example.app.ws.service;

import com.example.app.ws.shared.dto.NoteDto;

import java.util.List;

public interface INoteService {
    NoteDto createNote(NoteDto note);
    NoteDto getNoteByNoteId(String noteId);
    NoteDto updateNote(String noteId, NoteDto note);
    void deleteNote(String noteId);

    List<NoteDto> getMyNotes(String userId, boolean SourceNotes);
}
