package com.example.sms.model;

import android.net.Uri;

public class UserStudent {

    private String profileuri;
    private String fullName;
    private String nickName;
    private String dob;
    private String contactNo;
    private String email;
    private String address;



    public UserStudent() {
    }

    public UserStudent(String profileuri, String fullName, String nickName, String dob, String contactNo, String email, String address) {
        this.fullName = fullName;
        this.nickName = nickName;
        this.dob = dob;
        this.contactNo = contactNo;
        this.address = address;
        this.email = email;
        this.profileuri = profileuri;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileuri() {
        return profileuri;
    }

    public void setProfileuri(String profileuri) {
        this.profileuri = profileuri;
    }
}
