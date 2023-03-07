package com.theelitedevelopers.academia.modules.main.data.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Announcement {
    String id;
    String title;
    String description;
    String announcerId;
    String announcerName;
    Timestamp date;

    public Announcement(){}

    public Announcement(String title, String description, String announcerName, Timestamp date) {
        this.title = title;
        this.description = description;
        this.announcerName = announcerName;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnouncerId() {
        return announcerId;
    }

    public void setAnnouncerId(String announcerId) {
        this.announcerId = announcerId;
    }

    public String getAnnouncerName() {
        return announcerName;
    }

    public void setAnnouncerName(String announcerName) {
        this.announcerName = announcerName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
