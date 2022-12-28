package com.example.sms.model;

import java.io.Serializable;

public class Question {

    private String unitName;
    private String question;
    private String subjectName;
    private String timestamp;

    public Question() {
    }

    public Question(String unitName, String question, String subjectName, String timestamp) {
        this.unitName = unitName;
        this.question = question;
        this.subjectName = subjectName;
        this.timestamp = timestamp;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
