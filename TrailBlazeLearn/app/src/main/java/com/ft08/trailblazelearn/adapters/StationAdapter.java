package com.ft08.trailblazelearn.adapters;

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
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.StationActivity;
import com.ft08.trailblazelearn.models.Station;
import com.ft08.trailblazelearn.models.Trail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keerthanadevi on 13/3/18.
 */

public class StationAdapter extends ArrayAdapter<Station> {

    private Context context;
    private List<Station> stations = new ArrayList<Station>();
    private EditText stationName,GPS,instructions,sequenceNum;
    private Button addstationBtn;
    private String trailid,trailKey;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser refUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef  = database.getReference("Users").child(refUser.getUid()).child("Trails");
    DatabaseReference tkref;
    DatabaseReference sref;

    private Trail trail;

    public StationAdapter(Context context,String data) {
        super(context, R.layout.trail_row_layout);
        this.context = context;
        this.trailKey=data;
        tkref = myRef.child(trailKey);
        sref = tkref.child("Stations");
        refreshStations();
    }

    public void refreshStations() {

        tkref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //getData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getData(DataSnapshot dataSnapshot){
        stations.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Station station1=(Station) ds.getValue(Station.class);
            stations.add(station1);
            notifyDataSetChanged();
        }

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
                                sref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        sref.child(station.getStationID()).removeValue();
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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

                Station editstation =getItem(position);

                String seq=Integer.toString(editstation.getSeqNum());

                ((EditText) sview.findViewById(R.id.seqNumtxt)).setText(seq);
                ((EditText) sview.findViewById(R.id.stationNametxt)).setText(editstation.getStationName());
                ((EditText) sview.findViewById(R.id.gpstxt)).setText(editstation.getGps());
                ((EditText) sview.findViewById(R.id.instructionsTxt)).setText(editstation.getInstructions());

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

                            final Station edstation = new Station(seqno,stName,instinfo,location);

                            sref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    sref.child(station.getStationID()).setValue(edstation);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            dialog.dismiss();
                            Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.show();
                refreshStations();
            }
        });

        return convertView;

    }

    @Nullable
    @Override
    public Station getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override public int getCount() {
        return stations.size();
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
