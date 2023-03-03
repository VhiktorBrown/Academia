package com.theelitedevelopers.academia.modules.main.data.models;

public class Assignment {
    String courseCode;
    String courseName;
    String title;
    String image;
    String description;
    String datePosted;
    String dateDue;

    public Assignment(String courseCode, String title, String dateDue) {
        this.courseCode = courseCode;
        this.title = title;
        this.dateDue = dateDue;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDateDue() {
        return dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
    }
}
