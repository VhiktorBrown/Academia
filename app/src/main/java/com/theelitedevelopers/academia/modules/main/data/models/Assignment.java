package com.theelitedevelopers.academia.modules.main.data.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Assignment {
    String courseCode;
    String courseTitle;
    String title;
    String image;
    String description;
    String lecturerName;
    Timestamp datePosted;
    Timestamp dateDue;

    public Assignment(){}

    public Assignment(String courseCode, String title, Timestamp dateDue) {
        this.courseCode = courseCode;
        this.title = title;
        this.dateDue = dateDue;
    }

    public Assignment(String courseCode, String courseName, String title, String lecturerName, Timestamp dateDue) {
        this.courseCode = courseCode;
        this.courseTitle = courseName;
        this.title = title;
        this.lecturerName = lecturerName;
        this.dateDue = dateDue;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getImage() {
        return image;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
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

    public Timestamp getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Timestamp datePosted) {
        this.datePosted = datePosted;
    }

    public Timestamp getDateDue() {
        return dateDue;
    }

    public void setDateDue(Timestamp dateDue) {
        this.dateDue = dateDue;
    }
}
