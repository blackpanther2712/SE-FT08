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
    private View fragmentView,sview;
    private ListView stationList;
    private AlertDialog.Builder mBuilder;
    private AlertDialog dialog;
    private static String trailid, trailKey;
    private FloatingActionButton floatingActionButton;
    private String latLong,locationAddress;
    private DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Trails");
    private DatabaseReference tref=dRef.child(trailKey).getRef();

    public static void newInstance(String data, String passedTrailKey) {
        trailKey = passedTrailKey;
        trailid = data;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_trail, container, false);
        setUIComponents();
        onClickFab();
        return fragmentView;
    }

    public void setUIComponents(){
        stationList = (ListView) fragmentView.findViewById(R.id.trail_list);
        stationEmpty = (TextView) fragmentView.findViewById(R.id.empty_value);
        LocationsFragment.locationInstance(trailid,trailKey);
        stationAdapter = new StationAdapter(getContext(),trailid, trailKey, getActivity());
        stationList.setAdapter(stationAdapter);
        stationList.setEmptyView(stationEmpty);
        floatingActionButton = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
        floatingActionButton.setVisibility(App.user instanceof Participant ? View.GONE : View.VISIBLE);
    }

    public void onClickFab(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuilder = new AlertDialog.Builder(getContext());
                sview = getLayoutInflater().inflate(R.layout.add_station_dialogbox, null);
                setDialogBoxView();
                mBuilder.setView(sview);
                dialog = mBuilder.create();
                onClickLocation();
                onClickAddButton();
                dialog.show();
            }
        });
    }

    public void setDialogBoxView(){
        stationName = (EditText)sview.findViewById(R.id.stationNametxt);
        location = (EditText) sview.findViewById(R.id.gpstxt);
        instructions = (EditText) sview.findViewById(R.id.instructionsTxt);
        addstationBtn = (Button) sview.findViewById(R.id.CreateBtn);
    }

    public void onClickLocation(){
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
    }

    public void onClickAddButton(){
        addstationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    final String name = stationName.getText().toString().trim();
                    final String geo = latLong;
                    final String address =location.getText().toString().toString() ;
                    final String info = instructions.getText().toString().trim();
                    Station station=(App.trainer.getTrail(trailid)).addStation(stationAdapter.getCount()+1,name,info,geo,address);
                    DatabaseReference s2ref = tref.child("Stations").push();
                    String key = s2ref.getKey();
                    station.setStationID(key);
                    s2ref.setValue(station);
                    dialog.dismiss();
                    Toast.makeText(getContext(),getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

