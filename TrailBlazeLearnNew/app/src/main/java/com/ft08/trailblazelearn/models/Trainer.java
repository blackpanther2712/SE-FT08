package com.ft08.trailblazelearn.models;

import com.ft08.trailblazelearn.application.App;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trainer extends User {


    private ArrayList<Trail> trails;

     public Trainer(){
        trails = new ArrayList<>();
    }

     public Trainer(String userId, String name, String image) {
         super(userId, name, image);
         trails= new ArrayList<Trail>();
     }

    public void setTrails(ArrayList<Trail> trails) {
        this.trails = trails;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    public Trail getTrail(String trailID) {
        for (Trail trail : trails) {
            if (trail.getTrailID().equals(trailID)) {
                return trail;
            }
        }
        return null;
    }

    public void addTrail(Trail trail) {
         trails.add(trail);
    }

    public void setTrail(int index, Trail trail) {
        trails.set(index, trail);
    }

    public Trail addTrail(String trailName, String trailCode,String module, String trailDate, String userId) {
        Trail trail = new Trail(trailName, trailCode, module, trailDate, userId);
        trails.add(trail);
        return trail;
    }
    public void removeTrail(String trailID) {
        Trail trail = getTrail(trailID);
        if(trail!=null){
            trails.remove(trail);
        }
    }

    public void editTrail(String trailName, String code, String module, String trailDate, String oldTrailID, String newTrailId, String userId) {
         App.trainer.getTrail(oldTrailID).editTrail(trailName,code,module,trailDate,newTrailId,userId);
    }
}
