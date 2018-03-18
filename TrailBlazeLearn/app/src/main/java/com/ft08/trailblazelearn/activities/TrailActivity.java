package com.ft08.trailblazelearn.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.adapters.TrailAdapter;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.helpers.TrailHelper;
import com.ft08.trailblazelearn.models.Trail;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrailActivity extends AppCompatActivity {

    private EditText trailName, trailCode, module, trailDate, editedTrailCode, editedTrailDate;
    private FloatingActionButton floatingActionButton;
    private Calendar calendar = Calendar.getInstance();
    private TrailAdapter trailAdapter;
    private TextView trailEmptyText;
    private ArrayList<Trail> trails;
    private ArrayList<String> keys;
    public ListView trailListView;
    private Button addtrailBtn;
    private Date startDate;
    private View editedView;

    DatePickerDialog.OnDateSetListener date = null;
    AlertDialog.Builder mBuilder = null;
    AlertDialog dialog = null;

    private DatabaseReference currentTrialRef;
    private FirebaseDatabase database;
    private DatabaseReference dRef;
    private FirebaseUser user;
    private String editedTrailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail);
        initFirebase();
        attachDatabaseListener();
        initReferences();
        setFloatingActionButtonListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        trailEmptyText.setVisibility(trailAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }


    /*
    * Initializing All Firebase Instances
    * */
    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dRef = database.getReference("Trails");
    }


    /*
    * Initializing All Views In TrailActivity
    * */
    private void initReferences() {
        trails = new ArrayList<>();
        keys = new ArrayList<>();
        trailAdapter = new TrailAdapter(this, R.layout.trail_row_layout, trails);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        trailListView = (ListView) findViewById(R.id.trail_list);
        trailEmptyText = (TextView) findViewById(R.id.empty_value);
        trailListView.setEmptyView(trailEmptyText);
        trailListView.setAdapter(trailAdapter);
    }

    /*
    * Initializing All Views In Trail Details Dialog Box
    * */
    public void initDialogBoxViews(View mView) {
        trailName = (EditText) mView.findViewById(R.id.TrailNametxt);
        trailCode = (EditText) mView.findViewById(R.id.TrailCodetxt);
        module = (EditText) mView.findViewById(R.id.Moduletxt);
        trailDate = (EditText) mView.findViewById(R.id.datetxt);
        addtrailBtn = (Button) mView.findViewById(R.id.CreateBtn);
    }


    /*
    * Returns The TrailId In Order To Compare Ids Of Edited & Current Trail
    * */
    public String geTrailId(String currentTrailCode, Date currentTrailDate) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        return format.format(currentTrailDate) + "-" + currentTrailCode;
    }


    /*
    * Removes A Trail From The TrailList
    * */
    public void removeTrail(Trail removedTrail) {
        for(int i = 0; i < trails.size(); i++) {
            if(trails.get(i).getTrailCode() == removedTrail.getTrailCode()) {
                trails.remove(i);
                break;
            }
        }
    }

    /*
    * Attaching A Database Listener To Listen For Changes If:
    * 1. New Trail Is Added
    * 2. Some Changes In The Existing Trail
    * 3. A Trail Is Removed
    * */
    public void attachDatabaseListener() {
        dRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Trail addedTrail = dataSnapshot.getValue(Trail.class);
                trails.add(addedTrail);
                keys.add(dataSnapshot.getKey());
                trailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Trail changedTrail = dataSnapshot.getValue(Trail.class);
                String key = dataSnapshot.getKey();
                trails.set(keys.indexOf(key), changedTrail);
                trailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Trail removedTrail = dataSnapshot.getValue(Trail.class);
                keys.remove(dataSnapshot.getKey());
                removeTrail(removedTrail);
                trailAdapter.notifyDataSetChanged();
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    /*
    * This Listener Handles The Date Picker When Date Edit Text Is Clicked
    * */
    private void setTrailDateClickListener() {
        trailDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(TrailActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });
    }

    /*
    * This Method Sets The Listener For Save Button Of The Trail Details Dialog Box
    * Following Are The Functionalities:
    * 1. Check If Trail Code Or Date Has Been Edited
    *   - Yes : Then Delete The Old Trail & Insert A New One (Because Trail Id Is Dependant On Trail Code & Date)
    *   - No : Update The Existing Node
    * 2. Add The New Trail If It Is Complelely A New One
    * */
    private void setAddtrailBtnClickListener() {
        addtrailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = trailName.getText().toString().trim();
                String code = trailCode.getText().toString().trim();
                String moduleText = module.getText().toString().trim();
                String traildate = trailDate.getText().toString().trim();

                if(editedView != null) {
                    String currentTrailId = geTrailId(code, startDate);
                    if(currentTrailId.toString().trim().equals(editedTrailId.toString().trim())) {}
                    else { dRef.child(editedTrailId).removeValue();}
                }

                if(isValid(name, code, traildate)) {
                    Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                    Trail currentTrail = App.trainer.addTrail(name, code, moduleText,startDate);
                    String trail_id = currentTrail.getTrailID();
                    currentTrialRef = dRef.child(trail_id);
                    currentTrialRef.setValue(currentTrail);
                    currentTrialRef.child("Trail Date").setValue(new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH).format(startDate));
                    currentTrialRef.child("TimeStamp").setValue(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH).format(currentTimestamp));
                    dialog.dismiss();
                    Toast.makeText(TrailActivity.this,getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setDateClickListener() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try { startDate = formatter.parse(trailDate.getText().toString().trim());}
        catch (ParseException e) { e.printStackTrace(); }

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                trailDate.setText(sdf.format(calendar.getTime()));
                try { startDate = sdf.parse(trailDate.getText().toString().trim());}
                catch (ParseException e) { e.printStackTrace(); }
            }
        };
    }


    /*
    * This Method Pops Up The Dialog Box To Enter Details For The Trail.
    * 1. Checks If The The Trail Is Being Edited Or Newly Created
    *    - If Edited : Update The Same Node
    *    - If Newly Created : After Triggering Save Button Create A New Node In The Database
    * */
    public void popUpDialogBox(View passedView, int code) {
        mBuilder = new AlertDialog.Builder(TrailActivity.this);
        if(code == 0) {
            Date editedTrailDt = null;
            editedView = passedView;
            editedTrailCode = (EditText) editedView.findViewById(R.id.TrailCodetxt);
            editedTrailDate = (EditText) editedView.findViewById(R.id.datetxt);
            try { editedTrailDt = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(editedTrailDate.getText().toString().trim()); }
            catch (ParseException e) { e.printStackTrace(); }
            editedTrailId = geTrailId(editedTrailCode.getText().toString(), editedTrailDt);
        }
        else editedView = null;
        initDialogBoxViews(passedView);
        setDateClickListener();
        setTrailDateClickListener();
        setAddtrailBtnClickListener();
        mBuilder.setView(passedView);
        dialog = mBuilder.create();
        dialog.show();
    }


    /*
    * This Method Sets The Listener For The Floating Button Which Helps In Adding Trails
    * */
    private void setFloatingActionButtonListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mView = getLayoutInflater().inflate(R.layout.add_trail_dialogbox, null);
                popUpDialogBox(mView, 1);
            }
        });
    }


    /*
    * Validates If All Trail Details Have Been Entered Correctly
    * */
    private boolean isValid(String trailname, String trailcode, String traildate) {
        boolean isValid = true;
        if (TextUtils.isEmpty(trailname)) {
            trailName.setError(getString(R.string.trail_name_validation_ms));
            isValid = false;
        }
        if (TextUtils.isEmpty(trailcode)) {
            module.setError(getString(R.string.module_validation_ms));
            isValid = false;
        }
        if (TextUtils.isEmpty(trailcode)) {
            trailCode.setError(getString(R.string.code_validation_ms));
            isValid = false;
        }

        if (TextUtils.isEmpty(traildate)) {
            trailDate.setError(getString(R.string.date_validation_ms));
            isValid = false;
        }
        return isValid;
    }
}

