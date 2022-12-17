package com.example.sms.model;

public class Attendance {

    String uname;
    String status;

    public Attendance() {
    }

    public Attendance(String uname, String status) {
        this.uname = uname;
        this.status = status;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
