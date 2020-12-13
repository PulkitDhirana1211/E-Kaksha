package com.example.ekaksha.Data;

public class Examination {
    private String classroomID;
    private String classroomName;
    private String name;
    private String description;
    private  String url;
    private String timeStart;
    private int duration;
    private int maximumMarks;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMaximumMarks(int maximumMarks) {
        this.maximumMarks = maximumMarks;
    }

    public int getMaximumMarks() {
        return maximumMarks;
    }

    public String getClassroomID() {
        return classroomID;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public String getTimeStart() {
        return timeStart;
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


}
