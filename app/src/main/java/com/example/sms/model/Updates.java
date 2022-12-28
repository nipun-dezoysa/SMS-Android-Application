package com.example.sms.model;

public class Updates {

    private String updateID;
    private String title;
    private String content;

    public Updates() {
    }

    public Updates(String updateID, String title, String content) {
        this.updateID = updateID;
        this.title = title;
        this.content = content;
    }

    public String getUpdateID() {
        return updateID;
    }

    public void setUpdateID(String updateID) {
        this.updateID = updateID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
