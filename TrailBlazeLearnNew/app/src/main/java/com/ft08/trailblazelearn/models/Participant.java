package com.ft08.trailblazelearn.models;

public class Participant extends User {

    private String trailId;



    /*Default Constructor
   */
    public Participant() {}


    /*Constructor with userId,Name and Image as Parameter
     */
    public Participant(String userId, String name, String image) {
        super(userId, name, image);
    }


    /*
    Getters and setters for private variables
        trailId
     */
    public String getTrailId() { return trailId; }

    public void setTrailId(String trailId) { this.trailId = trailId; }


}
