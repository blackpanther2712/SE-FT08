package com.ft08.trailblazelearn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ft08.trailblazelearn.activities.StationActivity;
import com.ft08.trailblazelearn.adapters.StationAdapter;
import com.ft08.trailblazelearn.application.App;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.ft08.trailblazelearn.models.Station;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ft08.trailblazelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LocationsFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap googleMap;
    private List<String> stationName = new ArrayList<String>();
    public ArrayList<String> latLngs= new ArrayList<String>();
    private static String trailID,trailKey;
    private DatabaseReference dRef;
    private DatabaseReference gRef;

    public static void locationInstance(String data,String key){
        trailID=data;
        trailKey = key;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_locations, container, false);
        initFirebaseDatabaseRef();
        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latLngs.clear();
                stationName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds != null) {
                        Station station = (Station) ds.getValue(Station.class);
                        latLngs.add(station.getGps());
                        stationName.add(station.getStationName());
                    }
                }
                onMapReady(googleMap);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return fragmentView;
    }

    public void initFirebaseDatabaseRef(){
        dRef = FirebaseDatabase.getInstance().getReference("Trails");
        gRef=dRef.child(trailKey).child("Stations");
    }

    @Override
    public void onMapReady(GoogleMap goog) {
        googleMap = goog;
        int i=1,j=0;
        googleMap.clear();
        for(String place : latLngs){
            if(place!=null) {
                String nwPlace = trim(place, "lat/lng: (", ")");
                String[] loc = nwPlace.split(",");
                double latitude = Double.parseDouble(loc[0]);
                double longitutude = Double.parseDouble(loc[1]);
                LatLng location = new LatLng(latitude, longitutude);
                googleMap.addMarker(new MarkerOptions().position(location).title("Station" + "-" + i).snippet(stationName.get(j)));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                i++;j++;
            }
        }
    }

    public String trim(String place,String prefix,String suffix){
        int indexOfLast = place.lastIndexOf(suffix);
        place = place.substring(10, indexOfLast);
        return place;
    }

}
