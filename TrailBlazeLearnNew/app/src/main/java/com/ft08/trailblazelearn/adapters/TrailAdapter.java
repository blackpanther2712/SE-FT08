package com.ft08.trailblazelearn.adapters;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.StationActivity;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.ft08.trailblazelearn.activities.TrailActivity;
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
import com.google.firebase.database.Query;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class TrailAdapter extends ArrayAdapter<Trail> {

    private FirebaseDatabase database;
    private DatabaseReference rRef;
    private FirebaseUser refUser;

    public TrailAdapter(Context context, int resource, List<Trail> objects) {
        super(context, resource, objects);
        database = FirebaseDatabase.getInstance();
        refUser = FirebaseAuth.getInstance().getCurrentUser();
        rRef = database.getReference("Trails");
    }


    private ViewHolder getViewHolder(View convertView) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.trailName = (TextView) convertView.findViewById(R.id.trail_name);
        viewHolder.btnRemove = (ImageButton) convertView.findViewById(R.id.btn_remove);
        viewHolder.btnEdit = (ImageButton) convertView.findViewById(R.id.btn_edit);
        viewHolder.trailModule = (TextView) convertView.findViewById(R.id.trail_mod);
        viewHolder.trailDate = (TextView) convertView.findViewById(R.id.trail_date);
        return viewHolder;
    }


    @NonNull
    @Override public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trail_row_layout, parent, false);
            viewHolder = getViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else { viewHolder = (ViewHolder) convertView.getTag(); }

        final Trail trail = getItem(position);
        viewHolder.trailName.setText(trail.toString());
        viewHolder.trailModule.setText(trail.getModule());
        viewHolder.trailDate.setText(trail.getTrailDate());

        if(position % 2 == 0) { convertView.setBackgroundColor(getContext().getResources().getColor(R.color.cardview_shadow_start_color));}
        else { convertView.setBackgroundColor(getContext().getResources().getColor(R.color.cardview_shadow_end_color)); }

        viewHolder.trailName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StationActivity.class);
                intent.putExtra("trailId",trail.getTrailID());
                intent.putExtra("trailKey", trail.getTrailKey());
                getContext().startActivity(intent);
            }
        });

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                rRef.child(trail.getTrailKey()).removeValue();
                                remove(trail);
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete " + trail.getTrailName())
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TrailActivity currentTrailActivity = (TrailActivity) getContext();
                View view = inflater.inflate(R.layout.add_trail_dialogbox, null);
                Trail clickedTrail = getItem(position);
                ((EditText)view.findViewById(R.id.TrailNametxt)).setText(clickedTrail.getTrailName());
                ((EditText)view.findViewById(R.id.TrailCodetxt)).setText(clickedTrail.getTrailCode());
                ((EditText)view.findViewById(R.id.Moduletxt)).setText(clickedTrail.getModule());
                ((EditText)view.findViewById(R.id.datetxt)).setText(clickedTrail.getTrailDate());
                currentTrailActivity.popUpDialogBox(view, 0);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView trailName,trailModule,trailDate;
        ImageButton btnRemove;
        ImageButton btnEdit;
    }
}

