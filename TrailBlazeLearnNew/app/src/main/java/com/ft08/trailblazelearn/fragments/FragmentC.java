package com.ft08.trailblazelearn.fragments;

/**
 * Created by afaqueahmad on 17/3/18.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.ChooseContributedItemActivity;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.ft08.trailblazelearn.adapters.ContributedItemAdapter;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentC extends Fragment {

    private View fragmentView;
    private ContributedItemAdapter contributedItemAdapter;
    private DatabaseReference firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ChildEventListener childEventListener;
    private LinearLayoutManager linearLayoutManager;

    private ProgressDialog mProgressDialog;
    private RecyclerView blogList;
    private ArrayList<ContributedItem> contributedItem;
    private String currentTrailId;
    private String currentStationId;


    public FragmentC() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFirebaseDatabase();
        fragmentView= inflater.inflate(R.layout.fragment_c, container, false);
        return fragmentView;
    }

    private void initFirebaseDatabase() {
        currentStationId = ((SwipeTabsActivity)getActivity()).getCalledStationId();
        currentTrailId = ((SwipeTabsActivity)getActivity()).getCalledTrailKey();
        System.out.println (currentStationId);
        System.out.println (currentTrailId);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Trails");
        databaseReference = firebaseDatabase.child(currentTrailId).child("Stations").child(currentStationId).child("contributedItems");
    }

    private void initReferences() {

        contributedItem = new ArrayList<>();
        contributedItemAdapter = new ContributedItemAdapter(contributedItem,getContext());
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        blogList = (RecyclerView) fragmentView.findViewById(R.id.blog_list);
        blogList.setAdapter(contributedItemAdapter);
        blogList.setLayoutManager(linearLayoutManager);
        blogList.setHasFixedSize(true);

    }

    /*private void detachDatabaseListener() {
        if(childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }*/

    private void attachDatabaseListener() {

        System.out.println("I am inside attach database listener");

        if (childEventListener == null) {

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("LIFECYCLE","onchildAdded triggered");
                    ContributedItem cItem = dataSnapshot.getValue(ContributedItem.class);
                    contributedItem.add(cItem);
                    contributedItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            databaseReference.addChildEventListener(childEventListener);





        }
    }


    /*@Override
    public void onStart(){
        super.onStart();




    }*/

    @Override
    public void onResume() {
        attachDatabaseListener();
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initReferences();
        //attachDatabaseListener();

    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initReferences();
        attachDatabaseListener();
        blogList.setAdapter(contributedItemAdapter);

    }*/



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_action){
            Log.d("LIFECYCLE","+ button clicked");
            startActivity(new Intent(this.getContext(),ChooseContributedItemActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }



}

