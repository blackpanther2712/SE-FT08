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
import java.util.Locale;
import java.util.Date;

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
        Log.d("trail",myRef.getKey());
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
            viewHolder.trailModule = (TextView) convertView.findViewById(R.id.trail_mod);
            viewHolder.trailDate = (TextView) convertView.findViewById(R.id.trail_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Trail trail = getItem(position);
        viewHolder.trailName.setText(trail.toString());
        viewHolder.trailModule.setText(trail.getModule());
        Date tdate = trail.getTrailDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        viewHolder.trailDate.setText(sdf.format(tdate));

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.cardview_shadow_start_color));
        } else {
          convertView.setBackgroundColor(context.getResources().getColor(R.color.cardview_shadow_end_color));
        }

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

                                  rRef.child(trail.getTrailID()).removeValue();
                                  //refreshTrails();
                                  trails.remove(trail);
                                  notifyDataSetChanged();
//                                final Query query=rRef.orderByChild("trailID").equalTo(trail.getTrailID());
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

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View view = inflater1.inflate(R.layout.add_trail_dialogbox, null);
                mBuilder.setView(view);
                final AlertDialog dialog = mBuilder.create();

                Trail editTrail =getItem(position);
                Log.d("trail",editTrail.getTrailName());

                ((EditText) view.findViewById(R.id.TrailNametxt)).setText(editTrail.getTrailName());
                ((EditText) view.findViewById(R.id.TrailCodetxt)).setText(editTrail.getTrailCode());
                ((EditText) view.findViewById(R.id.Moduletxt)).setText(editTrail.getModule());
                Date tdate=editTrail.getTrailDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                ((EditText) view.findViewById(R.id.datetxt)).setText(sdf.format(tdate));

                trailName = (EditText) view.findViewById(R.id.TrailNametxt);
                trailCode = (EditText) view.findViewById(R.id.TrailCodetxt);
                module = (EditText) view.findViewById(R.id.Moduletxt);
                trailDate = (EditText) view.findViewById(R.id.datetxt);
                addtrailBtn = (Button) view.findViewById(R.id.CreateBtn);
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

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                            trailDate.setText(sdf.format(calendar.getTime()));
                            try {
                                stdate = sdf.parse(trailDate.getText().toString().trim());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            DateFormat form = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);


                            final Trail trail1 = new Trail(name, code, moduletxt,stdate);

                               rRef.child(trail.getTrailID()).removeValue();
                               DatabaseReference tref =myRef.child("Trails").child(trail1.getTrailID());
                               tref.setValue(trail1);
                               DateFormat ft = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                               Date d=trail1.getTrailDate();
                               rRef.child(trail1.getTrailID()).child("Trail Date").setValue(ft.format(d));
                               DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
                               Date d1=new Date();
                               Timestamp t =new Timestamp(d1.getTime());
                               rRef.child(trail1.getTrailID()).child("TimeStamp").setValue(form1.format(t));
                               notifyDataSetChanged();

//                            final Query query=rRef.orderByChild("trailID").equalTo(trail.getTrailID());
//                               query.addChildEventListener(new ChildEventListener() {
//                                       @Override
//                                   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                       Log.d("previous ",trail.getTrailName()+"-"+trail.getTrailID());
//                                       Log.d("previous key",dataSnapshot.getKey());
//
//                                       String key=dataSnapshot.getKey();
//                                       rRef.child(key).setValue(trail1);
//                                       DateFormat ft = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
//                                       Date d=trail1.getTrailDate();
//                                       rRef.child(key).child("Trail Date").setValue(ft.format(d));
//                                       DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
//                                       Date d1=new Date();
//                                       Timestamp t =new Timestamp(d1.getTime());
//                                       rRef.child(key).child("TimeStamp").setValue(form1.format(t));
//                                       notifyDataSetChanged();
//
//
//                                   }
//
//                                   @Override
//                                   public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                                   }
//
//                                   @Override
//                                   public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                   }
//
//                                   @Override
//                                   public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                                   }
//
//                                   @Override
//                                   public void onCancelled(DatabaseError databaseError) {
//
//                                   }
//                               });

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
        TextView trailName,trailModule,trailDate;
        ImageButton btnRemove;
        ImageButton btnEdit;

    }


}
