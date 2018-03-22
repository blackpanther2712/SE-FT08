package com.ft08.trailblazelearn.models;

import android.net.Uri;

import java.sql.Timestamp;

public class Post {

    private String text;
    private String name;
    private String photoUrl;
    private String ownerProfilePhotoUrl;

    Post() {}

    public Post(String text, String name, String photoUrl, String ownerProfilePhotoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.ownerProfilePhotoUrl = ownerProfilePhotoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOwnerProfilePhotoUrl() {
        return ownerProfilePhotoUrl;
    }

    public void setOwnerProfilePhotoUrl(String ownerProfilePhotoUrl) {
        this.ownerProfilePhotoUrl = ownerProfilePhotoUrl;
    }
}