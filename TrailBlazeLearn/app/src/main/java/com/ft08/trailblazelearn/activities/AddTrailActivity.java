package com.ft08.trailblazelearn.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.sql.Timestamp;
import java.text.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.ft08.trailblazelearn.models.Trail;
import com.ft08.trailblazelearn.models.Trainer;


import static java.util.Locale.US;

public class AddTrailActivity extends AppCompatActivity  {
    private EditText trailName,module,trailDate;
    private Calendar calendar;
    private Date startDate;
    private Trainer trainer;
    private DatabaseReference dRef;
    private FirebaseUser refUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_trail);
        trailName = (EditText) findViewById(R.id.et_trail_name);
        module = (EditText) findViewById(R.id.et_module);
        calendar = Calendar.getInstance();
        trailDate = (EditText)findViewById(R.id.et_date);
        refUser = FirebaseAuth.getInstance().getCurrentUser();

        dRef = FirebaseDatabase.getInstance().getReference().child(refUser.getUid());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
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
                new DatePickerDialog(AddTrailActivity.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {

                    /*Trail trail = new  Trail(trailName.getText().toString().trim(),
                            module.getText().toString().trim(), startDate);

                    DatabaseReference ref = dRef.child("Trails").push();
                    ref.child("TrailID").setValue(trail.getTrailID());
                    ref.child("TrailName").setValue(trail.getTrailName());
                    ref.child("Module").setValue(trail.getModule());
                    DateFormat form = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                    Date d=trail.getTrailDate();
                    ref.child("TrailDate").setValue(form.format(d));
*/
                    DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
                    Date d1=new Date();
                    Timestamp t =new Timestamp(d1.getTime());
                    //ref.child("TimeStamp").setValue(form1.format(t));

                    //ref.child("TimeStamp").setValue(ServerValue.TIMESTAMP);

                    Toast.makeText(AddTrailActivity.this,getString(R.string.saved_successfully),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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
