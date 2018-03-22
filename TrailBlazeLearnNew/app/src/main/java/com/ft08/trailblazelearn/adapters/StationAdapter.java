package com.ft08.trailblazelearn.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.EditStationActivity;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.Station;
import com.ft08.trailblazelearn.models.Trail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<Station> {

    private Context context;
    private ArrayList<Station> adaptstations = new ArrayList<Station>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference tkref;
    private DatabaseReference sref;
    private String trailid, trailKey;


    public StationAdapter(Context context, String data, String trailKey, Activity activity) {
        super(context, R.layout.trail_row_layout);
        this.context = context;
        this.trailid = data;
        this.trailKey = trailKey;
        initFirebaseDatabaseRef();
        refreshStations();
    }

    /*
    * Initializing All Firebase Instances
    * */
    public void initFirebaseDatabaseRef(){
        database = FirebaseDatabase.getInstance();
        myRef  = database.getReference("Trails");
        tkref = myRef.child(trailKey);
        sref = tkref.child("Stations");
    }

    /*
    * Refreshing the UI when there is some changes in station
    * like adding,editing and removing
    * */
    public void refreshStations() {
        sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getData(DataSnapshot dataSnapshot){
        adaptstations.clear();
        int i =1;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Station station1=(Station) ds.getValue(Station.class);
            station1.setSeqNum(i);
            sref.child(station1.getStationID()).child("seqNum").setValue(i);
            adaptstations.add(station1);
            i++;
            notifyDataSetChanged();
        }
        notifyDataSetChanged();
    }

    //This where we take care of core business logic...
    @NonNull
    @Override public View getView(final int position, View convertView, ViewGroup parent) {

        StationAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Inflate the layout for this adapter
            convertView = inflater != null ? inflater.inflate(R.layout.trail_row_layout, parent, false) : null;//This is related to view
            viewHolder = new StationAdapter.ViewHolder();
            /*
            *Initializing All Views In StationAdapter
            **/
            viewHolder.stationName = (TextView) convertView.findViewById(R.id.trail_name);
            viewHolder.btnRemove = (ImageButton) convertView.findViewById(R.id.btn_remove);
            viewHolder.btnEdit = (ImageButton) convertView.findViewById(R.id.btn_edit);
            viewHolder.btnEdit.setVisibility((App.user instanceof Participant) ? View.INVISIBLE : View.VISIBLE);
            viewHolder.btnRemove.setVisibility((App.user instanceof Participant) ? View.INVISIBLE : View.VISIBLE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StationAdapter.ViewHolder) convertView.getTag();
        }

        final Station station = getItem(position);
        viewHolder.stationName.setText(station.toString());

        /*
        * On clicking the station name, this listener navigated to SwipeActivity
        * with necessary data
        * */
        viewHolder.stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), SwipeTabsActivity.class);
                intent.putExtra("stationId",station.getStationID());
                intent.putExtra("trailId", trailid);
                intent.putExtra("trailKey", trailKey);
                intent.putExtra("stationName",station.getStationName());
                intent.putExtra("stationInstructions",station.getInstructions());
                intent.putExtra("stationLocation",station.getAddress());

                getContext().startActivity(intent);

            }
        });

        /*
        * This listener handles removing station functionality, from firebase and models
        * */
        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                sref.child(station.getStationID()).removeValue();
                                notifyDataSetChanged();
                                (App.trainer.getTrail(trailid)).removeStation(station.getStationID());
                                Trail trail = App.trainer.getTrail(trailid);
                                trail.getStations().clear();
                                trail.setStations(adaptstations);
                                notifyDataSetChanged();
                                dialog.dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete "+station.getStationName()).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                refreshStations();
            }
        });

        /*
        *On clicking the edit button, this listener navigated to EditStationActivity
        * with necessary data
        * This listener handles editing station functionality, in firebase and models
        *
        * */
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),EditStationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("stationId",station.getStationID());
                intent.putExtra("trailId", trailid);
                intent.putExtra("trailKey", trailKey);
                getContext().startActivity(intent);

                Trail trail = App.trainer.getTrail(trailid);
                trail.getStations().clear();
                refreshStations();
                trail.setStations(adaptstations);
            }
        });
        return convertView;
    }

    @Nullable
    @Override
    public Station getItem(int position) {
        return adaptstations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override public int getCount() {
        return adaptstations.size();
    }

    static class ViewHolder {
        TextView stationName;
        ImageButton btnRemove;
        ImageButton btnEdit;

    }

}
