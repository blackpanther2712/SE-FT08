package com.ft08.trailblazelearn.helpers;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.ft08.trailblazelearn.models.Trail;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by keerthanadevi on 12/3/18.
 */

public class TrailHelper {

    DatabaseReference db;
    Boolean saved;
    ArrayList<Trail> traillists = new ArrayList<>();

    public TrailHelper(DatabaseReference ref){
        this.db=ref;
    }

    public Boolean saveTrail(Trail trail){
        if(trail==null){
            saved=false;
        }
        else {
            try{

                DatabaseReference tref=db.child("Trails").push();
                tref.setValue(trail);
                DateFormat form = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date d=trail.getTrailDate();
                tref.child("Trail Date").setValue(form.format(d));
                DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
                Date d1=new Date();
                Timestamp t =new Timestamp(d1.getTime());
                tref.child("TimeStamp").setValue(form1.format(t));
                saved=true;

            }
            catch (DatabaseException e){
                e.printStackTrace();
                saved=false;
            }

        }
        return saved;

    }

    public void getData(DataSnapshot dataSnapshot){
        traillists.clear();
        for (DataSnapshot d : dataSnapshot.getChildren()) {
            Trail trail=d.getValue(Trail.class);
            traillists.add(trail);
        }

    }

    public ArrayList<Trail> retrieveData(){
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return traillists;
    }


}
