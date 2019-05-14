package com.example.app.ws.ui.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ErrorMessage {
    private Date timestamp;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-dd-MM HH:mm:ss.000 ", timezone="CET")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
