package com.ft08.trailblazelearn.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.models.Trail;
import com.ft08.trailblazelearn.models.Trainer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditTrailActivity extends AppCompatActivity {

    private EditText trailName,module,trailDate;
    private Calendar calendar;
    private Date startDate;
    private Trainer trainer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Trainers/Trails");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trail);
        Bundle bundle = getIntent().getExtras();
        final String trailID=bundle.getString("trailId");
        trainer=(Trainer) bundle.getSerializable("userobj");
        final Trail trail= trainer.getTrail(trailID);
        ((EditText) findViewById(R.id.et_trail_name)).setText(trail.getTrailName());
        ((EditText) findViewById(R.id.et_module)).setText(trail.getModule());
        Date dt=trail.getTrailDate();
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        ((EditText) findViewById(R.id.et_date)).setText(f.format(dt));

        trailName = (EditText) findViewById(R.id.et_trail_name);
        module = (EditText) findViewById(R.id.et_module);
        calendar = Calendar.getInstance();
        trailDate = (EditText) findViewById(R.id.et_date);
        SimpleDateFormat sdfh = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            startDate = sdfh.parse(trailDate.getText().toString().trim());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
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
                new DatePickerDialog(EditTrailActivity.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {

                    Trail trail1 =trainer.editTrail(trailName.getText().toString().trim(),
                            module.getText().toString().trim(),startDate,trailID);

                    DatabaseReference ref = myRef.child("key id");

                    Query query = ref.orderByChild("Trail ID").equalTo(trailID);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    ref.child("Trail ID").setValue(trail1.getTrailID());
                    ref.child("Trail Name").setValue(trail1.getTrailName());
                    ref.child("Module").setValue(trail1.getModule());
                    DateFormat form = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
                    Date d=trail1.getTrailDate();
                    ref.child("Trail Date").setValue(form.format(d));

                    DateFormat form1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
                    Date d1=new Date();
                    Timestamp t =new Timestamp(d1.getTime());
                    ref.child("TimeStamp").setValue(form1.format(t));

                    Toast.makeText(EditTrailActivity.this, getString(R.string.saved_successfully),
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

