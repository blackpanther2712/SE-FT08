package com.ft08.trailblazelearn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.fragments.StationFragment;
import com.ft08.trailblazelearn.models.Station;
import com.ft08.trailblazelearn.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by keerthanadevi on 20/3/18.
 */

public class EditStationActivity extends AppCompatActivity {

    private EditText gps, stationName, instructions;
    private Button addstationBtn;
    private String latLong,locationAddress;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Trails");
    private DatabaseReference tkref;
    private DatabaseReference sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_station_dialogbox);

        Bundle bundle = getIntent().getExtras();
        final String stId=bundle.getString("stationId");
        final String trailId=bundle.getString("trailId");
        tkref= myRef.child(trailId);
        sref = tkref.child("Stations");
        final Station station = (App.trainer.getTrail(trailId)).getStation(stId);

        String seq=Integer.toString(station.getSeqNum());
        ((TextView) findViewById(R.id.seqNumtxt)).setText(seq);
        ((EditText) findViewById(R.id.stationNametxt)).setText(station.getStationName());
        ((EditText) findViewById(R.id.gpstxt)).setText(station.getAddress());
        ((EditText) findViewById(R.id.instructionsTxt)).setText(station.getInstructions());

        stationName = (EditText) findViewById(R.id.stationNametxt);
        gps = (EditText) findViewById(R.id.gpstxt);
        instructions = (EditText) findViewById(R.id.instructionsTxt);
        addstationBtn = (Button) findViewById(R.id.CreateBtn);

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    Intent intent;
                    intent = builder.build(EditStationActivity.this);
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
                if(isValid()) {
                     String location = latLong;

                    final String stName = stationName.getText().toString().trim();
                    if(location==null){
                        location = station.getGps();
                    }
                    final String instinfo = instructions.getText().toString().trim();
                    final String address = gps.getText().toString().trim();

                    final Station edstation = (App.trainer.getTrail(trailId)).editStation(stName, instinfo, location,station.getStationID(),address);

                    DatabaseReference edRef = sref.child(station.getStationID());

                    edRef.child("stationName").setValue(edstation.getStationName());
                    edRef.child("instructions").setValue(edstation.getInstructions());
                    edRef.child("gps").setValue(edstation.getGps());
                    edRef.child("address").setValue(edstation.getAddress());

                    Toast.makeText(EditStationActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1){
            if(resultCode==RESULT_OK){

                Place place = PlacePicker.getPlace(EditStationActivity.this,data);
                latLong = place.getLatLng().toString();
                locationAddress = place.getAddress().toString();
                gps.setText(locationAddress);
            }
        }
    }

    private boolean isValid () {
        boolean isValid = true;
        if (TextUtils.isEmpty(stationName.getText().toString().trim())) {
            stationName.setError("Please fill in station name");
            isValid = false;
        }
        if (TextUtils.isEmpty(gps.getText().toString().trim())) {
            gps.setError("Please specify the location");
            isValid = false;
        }

        if (TextUtils.isEmpty(instructions.getText().toString().trim())) {
            instructions.setError("Please provide instruction");
            isValid = false;
        }
        return isValid;
    }
}
