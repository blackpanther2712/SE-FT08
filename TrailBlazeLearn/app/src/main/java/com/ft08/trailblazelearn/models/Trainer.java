package com.ft08.trailblazelearn.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trainer extends User {


    public ArrayList<Trail> trails;
//   public Trainer(){
//        trails = new ArrayList<>();
//    }

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

    public Trail addTrail(String trailName, String trailCode,String module, Date trailDate) {
        Trail trail = new Trail(trailName,trailCode,module,trailDate);
        trails.add(trail);
        return trail;
    }
    public void removeTrail(String trailID) {
        Trail trail = getTrail(trailID);
        if(trail!=null){
            trails.remove(trail);
        }
    }

    public Trail editTrail(String trailName, String module, Date trailDate,String trailID) {
        Trail trail = getTrail(trailID);
        return trail.editTrail(trailName,module,trailDate);

    }
}
