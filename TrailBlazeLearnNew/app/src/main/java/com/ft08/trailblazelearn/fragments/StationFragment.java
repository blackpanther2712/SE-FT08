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
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.Station;
import com.ft08.trailblazelearn.models.Trainer;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class StationFragment extends Fragment {
    private EditText stationName,location,instructions;
    private Button addstationBtn;
    private StationAdapter stationAdapter;
    private TextView stationEmpty;
    private static String trailid;
    private FloatingActionButton floatingActionButton;
    private String latLong,locationAddress;
    private DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Trails");
    private DatabaseReference tref=dRef.child(trailid).getRef();

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
        LocationsFragment.locationInstance(trailid);
        stationAdapter = new StationAdapter(getContext(),trailid,getActivity());
        stationList.setAdapter(stationAdapter);
        stationList.setEmptyView(stationEmpty);

        floatingActionButton =
                (FloatingActionButton) fragmentView.findViewById(R.id.fab);
        floatingActionButton.setVisibility(App.user instanceof Participant ? View.GONE : View.VISIBLE);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View sview = getLayoutInflater().inflate(R.layout.add_station_dialogbox, null);

                stationName = (EditText)sview.findViewById(R.id.stationNametxt);
                location = (EditText) sview.findViewById(R.id.gpstxt);
                instructions = (EditText) sview.findViewById(R.id.instructionsTxt);
                addstationBtn = (Button) sview.findViewById(R.id.CreateBtn);
                mBuilder.setView(sview);
                final  AlertDialog dialog = mBuilder.create();

                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                        try {
                            Intent intent;
                            intent = builder.build(getActivity());
                            startActivityForResult(intent, 1);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }


                    }
                });

                addstationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValid()){
                            final String name = stationName.getText().toString().trim();
                            final String geo = latLong;
                            final String address =location.getText().toString().toString() ;
                            final String info = instructions.getText().toString().trim();
                            Station station=(App.trainer.getTrail(trailid)).addStation(stationAdapter.getCount()+1,name,info,geo,address);

//                            DatabaseReference s2ref = tref.child("Stations").push();
//                            String key = s2ref.getKey();
//                            station.setStationID(key);
//                            s2ref.setValue(station);

                            DatabaseReference sref = tref.child("Stations").child(station.getSeqNum()+station.getStationName());
                            station.setStationID(station.getSeqNum()+station.getStationName());
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
        return fragmentView;
    }


    private boolean isValid () {
        boolean isValid = true;
        if (TextUtils.isEmpty(stationName.getText().toString().trim())) {
            stationName.setError(getString(R.string.station_name_validation_ms));
            isValid = false;
        }
        if (TextUtils.isEmpty(location.getText().toString().trim())) {
            location.setError(getString(R.string.location_validation_ms));
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
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1){
            if(resultCode==RESULT_OK){

                Place place = PlacePicker.getPlace(getActivity(),data);
                latLong = place.getLatLng().toString();
                locationAddress = place.getAddress().toString();
                location.setText(locationAddress);
            }
        }
    }

}

