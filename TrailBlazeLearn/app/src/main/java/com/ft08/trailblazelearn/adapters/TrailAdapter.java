package com.ft08.trailblazelearn.adapters;


import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.google.firebase.database.Query;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Timestamp;
import java.text.DateFormat;
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
    private Calendar calendar;
    private EditText trailName,trailCode,module,trailDate;
    private Button addtrailBtn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser refUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = database.getReference("Users").child(refUser.getUid());
    DatabaseReference rRef = myRef.child("Trails");



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
        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                final Query query=rRef.orderByChild("trailID").equalTo(trail.getTrailID());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {
                                            dataSnap.getRef().removeValue();
                                            notifyDataSetChanged();
                                        }
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
                builder.setMessage("Are you sure you want to delete "+trail.getTrailName()).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


                refreshTrails();
            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), EditTrailActivity.class);
//                intent.putExtra("trailId",trail.getTrailID());
//                getContext().startActivity(intent);

                final Dialog dialog = new Dialog(getContext());
                LayoutInflater inflater1 =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater1.inflate(R.layout.add_trail_dialogbox, null);

                dialog.setContentView(R.layout.add_trail_dialogbox);
                dialog.setTitle("Edit Trail Details");

                Trail editTrail =getItem(position);
                Log.d("trail",editTrail.getTrailName());

                ((EditText) v.findViewById(R.id.TrailNametxt)).setText(editTrail.getTrailName());
                ((EditText) v.findViewById(R.id.TrailCodetxt)).setText(editTrail.getTrailCode());
                ((EditText) v.findViewById(R.id.Moduletxt)).setText(editTrail.getModule());
                 Date tdate=editTrail.getTrailDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                ((EditText) v.findViewById(R.id.datetxt)).setText(sdf.format(tdate));

                trailName = (EditText) v.findViewById(R.id.TrailNametxt);
                trailCode = (EditText) v.findViewById(R.id.TrailCodetxt);
                module = (EditText) v.findViewById(R.id.Moduletxt);
                trailDate = (EditText) v.findViewById(R.id.datetxt);
                addtrailBtn = (Button) v.findViewById(R.id.CreateBtn);
                calendar = Calendar.getInstance();


                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        trailDate.setText(sdf.format(calendar.getTime()));
                        try {
                            stdate = sdf.parse(trailDate.getText().toString().trim());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };

                trailDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dpd = new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        dpd.show();
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

                    }
                });

                addtrailBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isValid()) {

                            final String name = trailName.getText().toString().trim();
                            final String code = trailCode.getText().toString().trim();
                            final String moduletxt = module.getText().toString().trim();

                            DateFormat form = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);


                            final Trail trail1 = new Trail(name, code, moduletxt,stdate);
//                            if (store.saveTrail(trail)){
//                                trailAdapter =new TrailAdapter(TrailActivity.this,store.retrieveData());
//                                trailList.setAdapter(trailAdapter);
//                            }

                            final Query query=rRef.orderByChild("trailID").equalTo(trail.getTrailID());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                        String key=dataSnapshot.getKey();
                                        rRef.child(key).setValue(trail1);
                                        DateFormat ft = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                                        Date d=trail1.getTrailDate();
                                        rRef.child(key).child("Trail Date").setValue(ft.format(d));
                                        DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
                                        Date d1=new Date();
                                        Timestamp t =new Timestamp(d1.getTime());
                                        rRef.child(key).child("TimeStamp").setValue(form1.format(t));

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
                refreshTrails();
            }
        });

        return convertView;
    }

    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(trailName.getText().toString().trim())) {
            trailName.setError("Please fill in trail name");
            isValid = false;
        }
        if (TextUtils.isEmpty(module.getText().toString().trim())) {
            module.setError("Please fill in module");
            isValid = false;
        }
        if (TextUtils.isEmpty(trailCode.getText().toString().trim())) {
            trailCode.setError("Please fill in trail code");
            isValid = false;
        }

        if (TextUtils.isEmpty(trailDate.getText().toString().trim())) {
            trailDate.setError("Select the date");
            isValid = false;
        }
        return isValid;
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
