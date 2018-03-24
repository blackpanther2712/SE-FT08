package com.ft08.trailblazelearn.models;

import com.ft08.trailblazelearn.application.App;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trainer extends User {


    private ArrayList<Trail> trails;


    /*Default Constructor
     */
     public Trainer(){
        trails = new ArrayList<>();
    }



    /*Constructor with userId,Name and Image as Parameter
     */
     public Trainer(String userId, String name, String image) {
         super(userId, name, image);
         trails= new ArrayList<Trail>();
     }


    /*
        Getters and setters for private variables
        1.trails
         */
    public List<Trail> getTrails() {
        return trails;
    }
    public void setTrails(ArrayList<Trail> trails) {
        this.trails = trails;
    }



    /*
    Getters and setters for accessing the individual trail in an arrayList
     */
    public void setTrail(int index, Trail trail) {
        trails.set(index, trail);
    }
    public Trail getTrail(String trailID) {
        for (Trail trail : trails) {
            if (trail.getTrailID().equals(trailID)) {
                return trail;
            }
        }
        return null;
    }


    /*
        Adding a Trail
     */
    public void addTrail(Trail trail) {
         trails.add(trail);
    }



    /*
        Updating a Trail from the List
     */
    public void editTrail(String trailName, String code, String module, String trailDate, String oldTrailID, String newTrailId, String userId) {
        App.trainer.getTrail(oldTrailID).editTrail(trailName,code,module,trailDate,newTrailId,userId);
    }



    /*
        Deleting a Trail from the List
     */
    public void removeTrail(String trailID) {
        Trail trail = getTrail(trailID);
        if(trail!=null){
            trails.remove(trail);
        }
    }


}
