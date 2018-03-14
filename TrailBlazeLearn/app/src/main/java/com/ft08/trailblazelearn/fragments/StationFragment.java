package com.ft08.trailblazelearn.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.adapters.StationAdapter;
import com.ft08.trailblazelearn.models.Station;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;


public class StationFragment extends Fragment {
    private EditText stationName,GPS,instructions;
    private Button addstationBtn;
    private StationAdapter stationAdapter;
    private TextView stationEmpty;
    private static String trailid;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dRef= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Trails");
    private DatabaseReference tref=dRef.child(trailid);
    //private static int sequenceNum=0;

    public static void newInstance(String data) {
        trailid=data;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.activity_trail, container, false);
        ListView stationList = (ListView) fragmentView.findViewById(R.id.trail_list);
        stationEmpty = (TextView) fragmentView.findViewById(R.id.empty_value);
        Log.d("frag tkey",trailid);
        stationAdapter = new StationAdapter(getContext(),trailid);
        stationList.setAdapter(stationAdapter);
        stationList.setEmptyView(stationEmpty);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) fragmentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View sview = getLayoutInflater().inflate(R.layout.add_station_dialogbox, null);

                stationName = (EditText)sview.findViewById(R.id.stationNametxt);
                GPS = (EditText) sview.findViewById(R.id.gpstxt);
                instructions = (EditText) sview.findViewById(R.id.instructionsTxt);
                addstationBtn = (Button) sview.findViewById(R.id.CreateBtn);
                mBuilder.setView(sview);
                final  AlertDialog dialog = mBuilder.create();

                addstationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValid()){
                            final String name = stationName.getText().toString().trim();
                            final String gps = GPS.getText().toString().trim();
                            final String info = instructions.getText().toString().trim();

                            Station station = new Station(stationAdapter.getCount()+1,name,info,gps);

                            DatabaseReference sref = tref.child("Stations").push();
                            String stId=sref.getKey();
                            station.setStationID(stId);
                            sref.setValue(station);

                            dialog.dismiss();
                            Toast.makeText(getContext(),getString(R.string.saved_successfully),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.show();

            }
        });
        //set visibility to add floating button
        //floatingActionButton.setVisibility(user instanceof Trainer? View.VISIBLE : View.GONE);
        return fragmentView;
    }


    private boolean isValid () {
        boolean isValid = true;
        if (TextUtils.isEmpty(stationName.getText().toString().trim())) {
            stationName.setError(getString(R.string.station_name_validation_ms));
            isValid = false;
        }
        if (TextUtils.isEmpty(GPS.getText().toString().trim())) {
            GPS.setError(getString(R.string.location_validation_ms));
            isValid = false;
        }

        if (TextUtils.isEmpty(instructions.getText().toString().trim())) {
            instructions.setError(getString(R.string.instruction_validation_ms));
            isValid = false;
        }
        return isValid;
    }


    @Override public void onResume() {
        super.onResume();
        stationAdapter.refreshStations();
        stationEmpty.setVisibility(stationAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        stationAdapter.refreshStations();
        stationEmpty.setVisibility(stationAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        stationAdapter.refreshStations();
        stationEmpty.setVisibility(stationAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }


}
