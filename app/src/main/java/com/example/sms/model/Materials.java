package com.example.sms.model;

public class Materials {

    private String referenceID;
    private String referenceLink;
    private String subject;
    private String unitName;

    public Materials() {
    }

    public Materials(String referenceID, String referenceLink, String subject, String unitName) {
        this.referenceID = referenceID;
        this.referenceLink = referenceLink;
        this.subject = subject;
        this.unitName = unitName;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
