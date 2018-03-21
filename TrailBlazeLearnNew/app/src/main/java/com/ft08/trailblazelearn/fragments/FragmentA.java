package com.ft08.trailblazelearn.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentA extends Fragment {

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
        txtStationLocation.setText("Venue: \n" + currentStationLocation);
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
}
