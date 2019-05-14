package com.example.app.ws.shared.dto;

import java.util.Date;

public class NoteDto {
private long id;
private String noteId;
private String header;
private String text;
private int priority;
private UserDto source;
private UserDto target;
private Date dateCreated;
private Date dateFrom;
private Date dateTo;
private GroupDto targetGroup;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public UserDto getSource() {
        return source;
    }

    public void setSource(UserDto source) {
        this.source = source;
    }

    public UserDto getTarget() {
        return target;
    }

    public void setTarget(UserDto target) {
        this.target = target;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public GroupDto getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(GroupDto targetGroup) {
        this.targetGroup = targetGroup;
    }
}
