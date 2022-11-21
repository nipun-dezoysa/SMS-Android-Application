package com.example.sms.model;

public class EditStudentDetails {



    private String email;
    private String contact;
    private String subject;
    private int grade;
    private String password;



    public EditStudentDetails(String uNameTxt, String editEmailTxt, String editContactTxt, String editSubjectTxt, int editGrade, String editpwdTxt){
        this.email = editEmailTxt;
        this.contact = editContactTxt;
        this.subject = editSubjectTxt;
        this.grade = editGrade;
        this.password = editpwdTxt;

    }

    public EditStudentDetails() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}


