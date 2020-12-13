package com.example.ekaksha.Data;

public class Assignment {
    private String classroomID;
    private String classroomName;
    private long Id;
    private String name;
    private String description;
    private  String url;
    private String deadline;

    public void setId(long id) {
        Id = id;
    }

    public long getId() {
        return Id;
    }

    public String getClassroomID() {
        return classroomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassroomID(String classroomID) {
        this.classroomID = classroomID;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
