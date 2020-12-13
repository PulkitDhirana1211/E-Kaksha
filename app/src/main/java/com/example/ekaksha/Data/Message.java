package com.example.ekaksha.Data;

public class Message {
    private String classroomID;
    private String userName;
    private String message;
    private String fileUrl;
    private long time;

    public String getFileUrl() {
        return fileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
