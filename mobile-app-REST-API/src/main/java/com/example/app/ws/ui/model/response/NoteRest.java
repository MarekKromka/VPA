package com.example.app.ws.ui.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class NoteRest {
    private String noteId;
    private String header;
    private String text;
    private int priority;
    private UserRest source;
    private UserRest target;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-dd-MM HH:mm:ss.000 ", timezone="CET")
    private Date dateCreated;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-dd-MM HH:mm:ss.000 ", timezone="CET")
    private Date dateFrom;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-dd-MM HH:mm:ss.000 ", timezone="CET")
    private Date dateTo;
    private GroupRest targetGroup;

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

    public UserRest getSource() {
        return source;
    }

    public void setSource(UserRest source) {
        this.source = source;
    }

    public UserRest getTarget() {
        return target;
    }

    public void setTarget(UserRest target) {
        this.target = target;
    }

    public GroupRest getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(GroupRest targetGroup) {
        this.targetGroup = targetGroup;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
