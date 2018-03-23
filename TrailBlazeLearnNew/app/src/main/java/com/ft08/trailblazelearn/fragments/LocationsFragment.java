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

    /*
    *Getting required data(TrailID & TrailKey) from StationFragment
    */
    public static void locationInstance(String data,String key){
        trailID=data;
        trailKey = key;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
    *This where we take care of core business logic...
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
         *Inflate the layout for this fragment
         */
        View fragmentView = inflater.inflate(R.layout.fragment_locations, container, false);//This is related to view
        initFirebaseDatabaseRef();
        /*
         *Obtain the SupportMapFragment and get notified when the map is ready to be used.
         */
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

    /*
    * Initializing All Firebase Instances
    * */
    public void initFirebaseDatabaseRef(){
        dRef = FirebaseDatabase.getInstance().getReference("Trails");
        gRef=dRef.child(trailKey).child("Stations");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                // Add a marker  and move the camera
                googleMap.addMarker(new MarkerOptions().position(location).title("Station" + "-" + i).snippet(stationName.get(j)));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                i++;j++;
            }
        }
    }

    /**
     * Trimming the GPS string to get exact latitude and Longitude
     */
    public String trim(String place,String prefix,String suffix){
        int indexOfLast = place.lastIndexOf(suffix);
        place = place.substring(10, indexOfLast);
        return place;
    }

}
