package com.ft08.trailblazelearn.application;

import android.app.Application;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class App extends Application {

    public static Trainer trainer;
    public static Participant participant;
    public static User user;
    public static HashMap<String,String> trailsKeyId = new HashMap<>();
    private FirebaseDatabase database;
    private DatabaseReference keyRef;

    public App() {

    }

    public App(Participant userparticipant) {
        participant=userparticipant;
        user = userparticipant;
        database = FirebaseDatabase.getInstance();
        keyRef = database.getReference("Keys");
        attachDatabaseListener();
    }

    public App(Trainer usertrainer){
        trainer = usertrainer;
        user = usertrainer;
        database = FirebaseDatabase.getInstance();
        keyRef = database.getReference("Keys");
        attachDatabaseListener();
    }

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
}
