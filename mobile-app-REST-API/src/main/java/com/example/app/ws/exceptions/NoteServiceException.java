package com.example.app.ws.exceptions;

public class NoteServiceException extends RuntimeException {
    public NoteServiceException(String message) {
        super(message);
    }
}
