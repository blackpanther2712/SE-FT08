package com.ft08.trailblazelearn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.ft08.trailblazelearn.models.Station;
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
    private MarkerOptions options=new MarkerOptions();
    private List<String> stationName = new ArrayList<String>();
    private List<Integer> seqNum = new ArrayList<Integer>();
    private ArrayList<String> latLngs= new ArrayList<>();
    private ArrayList<Station> stations = new ArrayList<Station>();
    private static String trailID;
    private DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Trails");
    private DatabaseReference gRef;

    public static void locationInstance(String data){
        trailID=data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_locations, container, false);

        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gRef=dRef.child(trailID).child("Stations");
        gRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                latLngs.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Station station=(Station) ds.getValue(Station.class);
                    stations.add(station);
//                    latLngs.add(station.getGps());
//                    stationName.add(station.getStationName());
//                    seqNum.add(station.getSeqNum());
                }
                //stations.clear();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//            Marker m = googleMap.addMarker(options.position(new LatLng(1.284218, 103.859299)).anchor(0.5f,0.5f)
//                    .title("Marina Bay").snippet("A"));
//       // }

        for (Station station : stations){
            latLngs.add(station.getGps());
        }

        latLngs.add("1.334585, 103.735510");
        latLngs.add("1.289316, 103.854502");

        return fragmentView;
    }

    @Override
    public void onMapReady(GoogleMap goog) {
        googleMap = goog;
        int i=1;
        for(String place : latLngs){

            String[] loc = place.split(",");
            double latitude = Double.parseDouble(loc[0]);
            double logitutude = Double.parseDouble(loc[1]);
            LatLng location = new LatLng(latitude,logitutude);
            googleMap.addMarker(new MarkerOptions().position(location).title("Station"+"-"+i));
           // googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            i++;

        }
    }


}
