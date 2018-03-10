package com.ft08.trailblazelearn.models;

import java.util.ArrayList;

public class Trainer extends User {

    private ArrayList<Trail> trailList;

    Trainer(String userId, String name, String image) {
        super(userId, name, image);
        trailList = new ArrayList<>();
    }

    public void addTrail(Trail newTrail) {
        trailList.add(newTrail);
    }

    public void deleteTrail(Trail trail) {
        trailList.remove(trail);
    }

    public ArrayList<Trail> getTrailList() {
        return trailList;
    }
}
