package com.ft08.trailblazelearn.models;

public class User {

    private String userId;
    private String name;
    private byte[] image;


    User(String userId, String name, byte[] image) {
        this.userId = userId;
        this.name = name;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
