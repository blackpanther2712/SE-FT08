package com.ft08.trailblazelearn.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.EditTrailActivity;
import com.ft08.trailblazelearn.activities.StationActivity;
import com.ft08.trailblazelearn.models.Trail;
import com.ft08.trailblazelearn.models.Trainer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Map;

/**
 * Created by keerthanadevi on 10,March,2018
 */
public class TrailAdapter extends ArrayAdapter<Trail> {

    private Context context;
    private ArrayList<Trail> trails = new ArrayList<Trail>();
    private Trainer trainer;
    Date stdate;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser refUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = database.getReference("Users").child(refUser.getUid());




    public TrailAdapter(Context context) {
        super(context, R.layout.trail_row_layout);
        this.context=context;
        //this.trails=traillist;
        refreshTrails();

    }

    public void refreshTrails() {

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);
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
        });


    }

    public void getData(DataSnapshot dataSnapshot){
        trails.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Trail trail1=(Trail) ds.getValue(Trail.class);
            trails.add(trail1);
            notifyDataSetChanged();
        }

    }




    @NonNull
    @Override public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater != null ? inflater.inflate(R.layout.trail_row_layout, parent, false) : null;
            viewHolder = new ViewHolder();
            viewHolder.trailName = (TextView) convertView.findViewById(R.id.trail_name);
            viewHolder.btnRemove = (ImageButton) convertView.findViewById(R.id.btn_remove);
            viewHolder.btnEdit = (ImageButton) convertView.findViewById(R.id.btn_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Trail trail = getItem(position);
        viewHolder.trailName.setText(trail.toString());

        viewHolder.trailName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), StationActivity.class);
                intent.putExtra("trailId",trail.getTrailID());
                getContext().startActivity(intent);
            }
        });
      /*  viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //trainer.removeTrail(trail.getTrailID());
                //need to include code to remove trail obj in firebase
                refreshTrails();


            }
        });*/
        /*viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditTrailActivity.class);
                intent.putExtra("trailId",trail.getTrailID());
                getContext().startActivity(intent);
                refreshTrails();
            }
        });*/

        return convertView;
    }

    @Nullable
    @Override
    public Trail getItem(int position) {
        return trails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override public int getCount() {
        return trails.size();
    }


    static class ViewHolder {
        TextView trailName;
        ImageButton btnRemove;
        ImageButton btnEdit;

    }


}
