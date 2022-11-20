package com.example.sms.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    private String username;
    private String contact;
    private String email;
    private String subject;
    private int grade;
    private boolean checkBoxSelected;

    public Student() {
    }

    public Student(String username, String contact, String email, String subject, int grade, boolean checkBoxSelected) {
        this.username = username;
        this.contact = contact;
        this.email = email;
        this.subject = subject;
        this.grade = grade;
        this.checkBoxSelected = checkBoxSelected;
    }


    public boolean getCheckBoxSelected() {
        return checkBoxSelected;
    }

    public void setCheckBoxSelected(boolean checkBoxSelected) {
        this.checkBoxSelected = checkBoxSelected;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
