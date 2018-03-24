package com.ft08.trailblazelearn.models;


public class User {

    private String userId;
    private String name;
    private String image;

    /*Default Constructor
     */
    public User() {}

    /*Constructor with userId,Name and Image as Parameter
     */
    public User(String userId, String name, String image) {
        this.userId = userId;
        this.name = name;
        this.image = image;
    }


    /*
    Getters and setters for private variables
    1.UserId
    2.Name
    3.Image
     */
    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
