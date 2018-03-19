package com.ft08.trailblazelearn.models;

public class Participant extends User {

    private String trailId;

    public Participant() {}

    public Participant(String userId, String name, String image) {
        super(userId, name, image);
    }

    public String getTrailId() { return trailId; }

    public void setTrailId(String trailId) { this.trailId = trailId; }

    public void joinTrail(String trailId) {}
}
