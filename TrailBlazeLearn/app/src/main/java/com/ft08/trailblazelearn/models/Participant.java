package com.ft08.trailblazelearn.models;
//Updated by Abhijit

public class Participant extends User{

    private String trailId;

    Participant(){


    }

    public String getTrailId() {
        return trailId;
    }

    public void setTrailId(String trailId) {
        this.trailId = trailId;
    }

    public void joinTrail(String trailId){

        //join trail using trailId
    }


}
