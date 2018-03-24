package com.ft08.trailblazelearn.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.ChooseContributedItemActivity;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.ft08.trailblazelearn.adapters.ContributedItemAdapter;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.ft08.trailblazelearn.models.Participant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentC extends Fragment {

    private View fragmentView;
    private ContributedItemAdapter contributedItemAdapter;
    private DatabaseReference firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton floatingActionButton;
    private RecyclerView blogList;
    private ArrayList<ContributedItem> contributedItem;
    private String currentTrailId;
    private String currentStationId;

    public FragmentC() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFirebaseDatabase();
        fragmentView = inflater.inflate(R.layout.fragment_c, container, false);
        floatingActionButton = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChooseContributedItemActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        floatingActionButton.setVisibility((App.user instanceof Participant) ? View.VISIBLE : View.INVISIBLE);
        return fragmentView;
    }


    private void initFirebaseDatabase() {
        currentStationId = ((SwipeTabsActivity) getActivity()).getCalledStationId();
        currentTrailId = ((SwipeTabsActivity) getActivity()).getCalledTrailKey();
        System.out.println(currentStationId);
        System.out.println(currentTrailId);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Trails");
        databaseReference = firebaseDatabase.child(currentTrailId).child("Stations").child(currentStationId).child("contributedItems");
    }

    private void initReferences() {
        contributedItem = new ArrayList<>();
        contributedItemAdapter = new ContributedItemAdapter(contributedItem, getContext());
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        blogList = (RecyclerView) fragmentView.findViewById(R.id.blog_list);
        blogList.setAdapter(contributedItemAdapter);
        blogList.setLayoutManager(linearLayoutManager);
        blogList.setHasFixedSize(true);

    }

    private void attachDatabaseListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ContributedItem cItem = dataSnapshot.getValue(ContributedItem.class);
                    contributedItem.add(cItem);
                    contributedItemAdapter.notifyDataSetChanged();
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }

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
    }
}

