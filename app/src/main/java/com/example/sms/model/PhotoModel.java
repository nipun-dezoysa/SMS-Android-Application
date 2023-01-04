package com.example.sms.model;

public class PhotoModel {

    private String imageUrl;
    public PhotoModel(){

    }
    public PhotoModel(String imageUrl){
        this.imageUrl = imageUrl;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
