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
import android.widget.TextView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.EditTrailActivity;
import com.ft08.trailblazelearn.activities.StationActivity;
import com.ft08.trailblazelearn.models.Trail;
import com.ft08.trailblazelearn.models.Trainer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by keerthanadevi on 10,March,2018
 */
public class TrailAdapter extends ArrayAdapter<Trail> {

    private Context context;
    private List<Trail> trails = new ArrayList<Trail>();
    private Trainer trainer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    public TrailAdapter(Context context) {
        super(context, R.layout.trail_row_layout);
        this.context=context;
        refreshTrails();
    }

    public void refreshTrails() {

        trails.clear();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot i:dataSnapshot.getChildren()) {
//                    trails.add(i.getValue(Trail.class));
//                    notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        trails.addAll(trainer .getTrails()); //need to use currentuser ref from firebase
        notifyDataSetChanged();


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
            viewHolder.btnRemove = (Button) convertView.findViewById(R.id.btn_remove);
            viewHolder.btnEdit = (Button) convertView.findViewById(R.id.btn_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Trail trail = trails.get(position);
        viewHolder.trailName.setText(trail.toString());

        viewHolder.trailName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), StationActivity.class);
                intent.putExtra("trailId",trail.getTrailID());
                getContext().startActivity(intent);
            }
        });
        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                trainer.removeTrail(trail.getTrailID());
                //need to include code to remove trail obj in firebase
                refreshTrails();
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditTrailActivity.class);
                intent.putExtra("trailId",trail.getTrailID());
                getContext().startActivity(intent);
                refreshTrails();
            }
        });

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
        Button btnRemove;
        Button btnEdit;

    }


}
