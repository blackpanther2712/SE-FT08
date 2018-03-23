package com.ft08.trailblazelearn.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.ft08.trailblazelearn.models.Post;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentA extends Fragment implements View.OnClickListener {

    private static String currentStationId;
    private static String currentStationName;
    private static String currentStationInstructions;
    private static String currentStationLocation;
    private static String currentTrailID;

    private View fragmentView;

    private TextView txtStationLocation,txtStationName,txtStationInstruction,txtTrailID;

    public FragmentA() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_a, container, false);
        initFirebaseDatabase();


        return fragmentView;
    }

    public void initUserInterfaceComponents()
    {
        txtStationName =(TextView)fragmentView.findViewById(R.id.station_name_label);
        txtStationLocation =(TextView)fragmentView.findViewById(R.id.location_label);
        txtStationInstruction =(TextView)fragmentView.findViewById(R.id.instructions_detail);
        txtTrailID =(TextView)fragmentView.findViewById(R.id.trail_label);


        txtStationName.setText("Name: \n" + currentStationName);
        txtStationInstruction.setText(currentStationInstructions);
        txtTrailID.setText("Trail ID: \n" + currentTrailID);
        txtStationLocation.setText( currentStationLocation);
        txtStationLocation.setPaintFlags(txtStationLocation.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        txtStationLocation.setOnClickListener(this);
    }
    private void initFirebaseDatabase() {


        currentStationId = ((SwipeTabsActivity)getActivity()).getCalledStationId();
        System.out.println (currentStationId);

        currentStationName = ((SwipeTabsActivity)getActivity()).getCalledStationName();
        System.out.println (currentStationName);

        currentStationInstructions = ((SwipeTabsActivity)getActivity()).getCalledStationInstructions();
        System.out.println (currentStationInstructions);

        currentStationLocation = ((SwipeTabsActivity)getActivity()).getCalledStationLocation() ;
        System.out.println (currentStationLocation);

        currentTrailID =  ((SwipeTabsActivity)getActivity()).getCalledTrailId();
        System.out.println (currentTrailID);

        initUserInterfaceComponents();

        //firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Trails");
        //databaseReference = firebaseDatabase.child(currentTrailId).child("Stations").child(currentStationId).child("contributedItems");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUserInterfaceComponents();
    }
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.location_label) {
            // Create a Uri from an intent string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + currentStationLocation+ "&travelmode=driving");

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);


        }
    }
}
