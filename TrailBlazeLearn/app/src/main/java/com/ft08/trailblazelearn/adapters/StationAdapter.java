package com.ft08.trailblazelearn.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.Station;
import com.ft08.trailblazelearn.models.Trail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by keerthanadevi on 13/3/18.
 */

public class StationAdapter extends ArrayAdapter<Station> {

    private Context context;
    private ArrayList<Station> adaptstations = new ArrayList<Station>();
    private EditText stationName,GPS,instructions,sequenceNum;
    private Button addstationBtn;
    private String trailid;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser refUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef  = database.getReference("Trails");
    DatabaseReference tkref;
    DatabaseReference sref;
    DatabaseReference partRef = database.getReference("Trails");

    private Trail trail;

    public StationAdapter(Context context,String data) {
        super(context, R.layout.trail_row_layout);
        this.context = context;
        this.trailid=data;
        tkref = myRef.child(trailid);
        sref = tkref.child("Stations");
        partRef = myRef.child(this.trailid);
        refreshStations();
    }

    public void refreshStations() {

        sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        tkref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                getData(dataSnapshot.child("Stations"));
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                 getData(dataSnapshot);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        sref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                getData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void getData(DataSnapshot dataSnapshot){

        adaptstations.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Station station1=(Station) ds.getValue(Station.class);
            adaptstations.add(station1);
            notifyDataSetChanged();
        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override public View getView(final int position, View convertView, ViewGroup parent) {

        StationAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater != null ? inflater.inflate(R.layout.trail_row_layout, parent, false) : null;
            viewHolder = new StationAdapter.ViewHolder();
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

        viewHolder.stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getContext(), );
//                intent.putExtra("stationId",station.getStationID());
//                getContext().startActivity(intent);
            }
        });

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

//                                final Query query=sref.orderByChild("stationID").equalTo(station.getStationID());
//                                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {
//                                            dataSnap.getRef().removeValue();
//                                            notifyDataSetChanged();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });


//                                sref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        sref.child(station.getStationID()).removeValue();
//                                        notifyDataSetChanged();
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });

                                sref.child(station.getStationID()).removeValue();

                                (App.trainer.getTrail(trailid)).removeStation(station.getStationID());
                                Trail trail = App.trainer.getTrail(trailid);
                                trail.getStations().clear();
                                refreshStations();


                                notifyDataSetChanged();
                                trail.setStations(adaptstations);



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

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View sview = inflater1.inflate(R.layout.edit_station_dialogbox, null);
                mBuilder.setView(sview);
                final AlertDialog dialog = mBuilder.create();

                //final Station editstation =getItem(position);

                String seq=Integer.toString(station.getSeqNum());

                ((EditText) sview.findViewById(R.id.seqNumtxt)).setText(seq);
                ((EditText) sview.findViewById(R.id.stationNametxt)).setText(station.getStationName());
                ((EditText) sview.findViewById(R.id.gpstxt)).setText(station.getGps());
                ((EditText) sview.findViewById(R.id.instructionsTxt)).setText(station.getInstructions());

                sequenceNum = (EditText) sview.findViewById(R.id.seqNumtxt);
                stationName = (EditText) sview.findViewById(R.id.stationNametxt);
                GPS = (EditText) sview.findViewById(R.id.gpstxt);
                instructions = (EditText) sview.findViewById(R.id.instructionsTxt);
                addstationBtn = (Button) sview.findViewById(R.id.CreateBtn);




                addstationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValid()){

                            final int seqno = Integer.parseInt(sequenceNum.getText().toString().trim());
                            final String stName = stationName.getText().toString().trim();
                            final String location = GPS.getText().toString().trim();
                            final String instinfo = instructions.getText().toString().trim();

                            final Station edstation =(App.trainer.getTrail(trailid)).editStation(seqno,stName,instinfo,location,station.getStationID());

                            sref.child(station.getStationID()).removeValue();

                            DatabaseReference stRef = sref.child(edstation.getStationID());
                            stRef.setValue(edstation);
                            notifyDataSetChanged();

//                            sref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                    sref.child(station.getStationID()).setValue(edstation);
//                                    sref.child(station.getStationID()).child("stationID").setValue(station.getStationID());
//                                    notifyDataSetChanged();
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });

                            dialog.dismiss();
                            Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.show();
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


    private boolean isValid () {
        boolean isValid = true;
        if (TextUtils.isEmpty(stationName.getText().toString().trim())) {
            stationName.setError("Please fill in station name");
            isValid = false;
        }
        if (TextUtils.isEmpty(GPS.getText().toString().trim())) {
            GPS.setError("Please specify the location");
            isValid = false;
        }

        if (TextUtils.isEmpty(instructions.getText().toString().trim())) {
            instructions.setError("Please provide instruction");
            isValid = false;
        }

        if (TextUtils.isEmpty(sequenceNum.getText().toString().trim())) {
            sequenceNum.setError("Please fill in sequence number");
            isValid = false;
        }
        return isValid;
    }

    static class ViewHolder {
        TextView stationName;
        ImageButton btnRemove;
        ImageButton btnEdit;

    }


}
