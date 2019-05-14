package com.example.app.ws.exceptions;

import com.example.app.ws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;

@EnableWebMvc
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException e, WebRequest req){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NoteServiceException.class})
    public ResponseEntity<Object> handleNoteServiceException(NoteServiceException e, WebRequest req){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {GroupServiceException.class})
    public ResponseEntity<Object> handleGroupServiceException(GroupServiceException e, WebRequest req){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {SubjectServiceException.class})
    public ResponseEntity<Object> handleSubjectServiceException(SubjectServiceException e, WebRequest req){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception e, WebRequest req){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
