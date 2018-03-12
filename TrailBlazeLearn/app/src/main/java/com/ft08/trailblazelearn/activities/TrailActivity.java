package com.ft08.trailblazelearn.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.adapters.TrailAdapter;
import com.ft08.trailblazelearn.helpers.TrailHelper;
import com.ft08.trailblazelearn.models.Trail;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TrailActivity extends AppCompatActivity {
    private EditText trailName,trailCode,module,trailDate;
    private Button addtrailBtn;
    private TextView trailEmpty;
    private TrailAdapter trailAdapter;

    private DatabaseReference dRef;
    private FirebaseUser user;
    private Calendar calendar= Calendar.getInstance();
    private Date startDate;
    //private TrailHelper store;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        //store = new TrailHelper(dRef);

        final ListView trailList = (ListView) findViewById(R.id.trail_list);
        trailEmpty = (TextView) findViewById(R.id.empty_value);
        //trailAdapter = new TrailAdapter(this,store.retrieveData());

        trailAdapter = new TrailAdapter(this);
        trailList.setAdapter(trailAdapter);
        trailList.setEmptyView(trailEmpty);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TrailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_trail_dialogbox, null);



                trailName = (EditText) mView.findViewById(R.id.TrailNametxt);
                trailCode = (EditText) mView.findViewById(R.id.TrailCodetxt);
                module = (EditText) mView.findViewById(R.id.Moduletxt);
                trailDate = (EditText) mView.findViewById(R.id.datetxt);
                addtrailBtn = (Button) mView.findViewById(R.id.CreateBtn);
                mBuilder.setView(mView);
                final  AlertDialog dialog = mBuilder.create();




                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        trailDate.setText(sdf.format(calendar.getTime()));
                        try {
                            startDate = sdf.parse(trailDate.getText().toString().trim());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                };
                trailDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       DatePickerDialog dpd = new DatePickerDialog(TrailActivity.this, date, calendar.get(Calendar.YEAR),
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


                            Trail trail = new Trail(name, code, moduletxt,startDate);
//                            if (store.saveTrail(trail)){
//                                trailAdapter =new TrailAdapter(TrailActivity.this,store.retrieveData());
//                                trailList.setAdapter(trailAdapter);
//                            }


                            DatabaseReference tref =dRef.child("Trails").push();
                            tref.setValue(trail);
                            DateFormat ft = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                            Date d=trail.getTrailDate();
                            tref.child("Trail Date").setValue(ft.format(d));

                            DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
                            Date d1=new Date();
                            Timestamp t =new Timestamp(d1.getTime());
                            tref.child("TimeStamp").setValue(form1.format(t));


                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            dialog.dismiss();
                            Toast.makeText(TrailActivity.this,getString(R.string.saved_successfully),
                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });
                 dialog.show();


            }
        });
    }




            @Override
            public void onResume() {
                super.onResume();
                trailAdapter.refreshTrails();
                //store.retrieveData();
                trailEmpty.setVisibility(trailAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onStop() {
                super.onStop();
                trailAdapter.refreshTrails();
                //store.retrieveData();
                trailEmpty.setVisibility(trailAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            protected void onStart() {
                super.onStart();
                trailAdapter.refreshTrails();
                //store.retrieveData();
                trailEmpty.setVisibility(trailAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
            }

    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(trailName.getText().toString().trim())) {
            trailName.setError(getString(R.string.trail_name_validation_ms));
            isValid = false;
        }
        if (TextUtils.isEmpty(module.getText().toString().trim())) {
            module.setError(getString(R.string.module_validation_ms));
            isValid = false;
        }

        if (TextUtils.isEmpty(trailDate.getText().toString().trim())) {
            trailDate.setError(getString(R.string.date_validation_ms));
            isValid = false;
        }
        return isValid;
    }
}
