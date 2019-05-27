package com.example.notdefteri.Entity;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Note {
    private String id;
    private String title;
    private String body;
    private int priority;
    private String color ; // sonra enum olucak
    private Date timestamp;
    private boolean reminder;
    private Date remindTime;

    public Note(String id) {
        this.id = id;
    }

    public Note(){}


    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @ServerTimestamp
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @ServerTimestamp
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date date) {
        this.timestamp = date;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }
}
