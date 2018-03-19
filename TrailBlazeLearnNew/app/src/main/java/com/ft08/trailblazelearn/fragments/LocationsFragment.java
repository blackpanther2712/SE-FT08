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
    private MarkerOptions options=new MarkerOptions();
    private List<String> stationName = new ArrayList<String>();
    private List<Integer> seqNum = new ArrayList<Integer>();
    public ArrayList<String> latLngs= new ArrayList<String>();
    private  ArrayList<Station> stations = new ArrayList<Station>();
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
                    latLngs.add(station.getGps());

                    //1.297802499999996,103.77531640624997
                    //1.2983024999999986,103.77621484374995
                    //1.3005324999999957,103.77473046874994


//                    stationName.add(station.getStationName());
//                    seqNum.add(station.getSeqNum());
                }
                //stations.clear();

                onMapReady(googleMap);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//            Marker m = googleMap.addMarker(options.position(new LatLng(1.284218, 103.859299)).anchor(0.5f,0.5f)
//                    .title("Marina Bay").snippet("A"));
//       // }



//        latLngs.add("1.334585, 103.735510");
//        latLngs.add("1.289316, 103.854502");
//        latLngs.add("1.297802499999996,103.77531640624997");
//        latLngs.add("1.2983024999999986,103.77621484374995");
//        latLngs.add("1.3005324999999957,103.77473046874994");




        return fragmentView;
    }

    @Override
    public void onMapReady(GoogleMap goog) {
        googleMap = goog;
        int i=1;
        for(String place : latLngs){
            String nwPlace = trim(place,"lat/lng: (",")");
            String[] loc = nwPlace.split(",");

            double latitude = Double.parseDouble(loc[0]);
            double longitutude = Double.parseDouble(loc[1]);
            LatLng location = new LatLng(latitude,longitutude);
            googleMap.addMarker(new MarkerOptions().position(location).title("Station"+"-"+i));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            i++;

        }
    }

    public String trim(String place,String prefix,String suffix){

        int indexOfLast = place.lastIndexOf(suffix);
        place = place.substring(10, indexOfLast);
        return place;

    }


}
