package com.ft08.trailblazelearn.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User {

    private String userId;
    private String name;
    private String image;
    public static HashMap<String,String> trailsKeyId = new HashMap<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference keyRef = database.getReference("Keys");

    public User(){


    }

    public User(String userId, String name, String image) {
        this.userId = userId;
        this.name = name;
        this.image = image;
        attachDatabaseListener();
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


//    public static HashMap<String, String> getTrailsKeyId() {
//        return trailsKeyId;
//    }
//
//    public static void setTrailsKeyId(HashMap<String, String> trailsKeyId) {
//        User.trailsKeyId.clear();
//        if(trailsKeyId!=null) {
//            User.trailsKeyId = trailsKeyId;
//        }
//    }

    public void attachDatabaseListener(){
        keyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trailsKeyId.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    trailsKeyId.put(ds.getKey(),(String) ds.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setImage(String image) {
        this.image = image;
    }

}
