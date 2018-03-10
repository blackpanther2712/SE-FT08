package com.ft08.trailblazelearn.models;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String userId;
    private String name;
    private String image;
    public ArrayList<Trail> trails;

    User(){


    }

    User(String userId, String name, String image) {
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

    public String getImage() {
        return image;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Trail getTrail(String trailID) {
        for (Trail trail : trails) {
            if (trail.getTrailID().equals(trailID)) {
                return trail;
            }
        }
        return null;
    }

}
