package com.example.app.ws.io.entity;

import com.example.app.ws.shared.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "notes")
public class NoteEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String noteId;

    @Column(nullable = false, length = 50)
    private String header;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false, columnDefinition = "int default 100")
    private int priority;

    @ManyToOne
    @JoinColumn(name = "users_id_source")
    private UserEntity source;

    @ManyToOne
    @JoinColumn(name = "users_id_target")
    private UserEntity target;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date dateFrom;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "groups_id_target")
    private GroupEntity targetGroup;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
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

    public UserEntity getSource() {
        return source;
    }

    public void setSource(UserEntity source) {
        this.source = source;
    }

    public UserEntity getTarget() {
        return target;
    }

    public void setTarget(UserEntity target) {
        this.target = target;
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

    public GroupEntity getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(GroupEntity targetGroup) {
        this.targetGroup = targetGroup;
    }
}
