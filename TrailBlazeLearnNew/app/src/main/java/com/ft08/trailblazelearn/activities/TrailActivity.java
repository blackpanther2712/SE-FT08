package com.ft08.trailblazelearn.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
//import com.ft08.trailblazelearn.helpers.TrailHelper;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TrailActivity extends AppCompatActivity {

    private EditText trailName, trailCode, module, trailDate;
    private String editedTrailCode, editedTrailDate, editedTrailName, editedTrailModule;
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
    private String oldTrailId, newTrailId;

    DatePickerDialog.OnDateSetListener date = null;
    AlertDialog.Builder mBuilder = null;
    AlertDialog dialog = null;

    private DatabaseReference currentTrialRef;
    private FirebaseDatabase database;
    private DatabaseReference dRef;
    private DatabaseReference keyRef;
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
        keyRef = database.getReference("Keys");
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
    * Checks If The Added/Deleted/Changed Trail Belongs To The Current User
    * */
    private boolean checkTrailUser(String trailUserId, String currentUserId) {
        if(trailUserId.equals(currentUserId))
            return true;
        return false;
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
                if(checkTrailUser(addedTrail.getuserId(), user.getUid())) {
                    trails.add(addedTrail);
                    keys.add(dataSnapshot.getKey());
                    trailAdapter.notifyDataSetChanged();
                    App.trainer.addTrail(addedTrail);
                }
                keyRef.child(addedTrail.getTrailID()).setValue(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Trail changedTrail = dataSnapshot.getValue(Trail.class);
                if(checkTrailUser(changedTrail.getuserId(), user.getUid())) {
                    String key = dataSnapshot.getKey();
                    trails.set(keys.indexOf(key), changedTrail);
                    App.trainer.setTrail(keys.indexOf(key), changedTrail);
                    trailAdapter.notifyDataSetChanged();
                }
                if(oldTrailId!=null) {
                    keyRef.child(oldTrailId).removeValue();
                }
                    keyRef.child(changedTrail.getTrailID()).setValue(dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Trail removedTrail = dataSnapshot.getValue(Trail.class);
                if(checkTrailUser(removedTrail.getuserId(), user.getUid())) {
                    keys.remove(dataSnapshot.getKey());
                    removeTrail(removedTrail);
                    App.trainer.removeTrail(removedTrail.getTrailID());
                    trailAdapter.notifyDataSetChanged();
                }
                keyRef.child(removedTrail.getTrailID()).removeValue();
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
    * This Method Adds Trails In The Database
    * */
    private void addTrailToDB(String trail_id, Trail trail) {
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        String pushKey = dRef.push().getKey();
        trail.setTrailKey(pushKey);
        currentTrialRef = dRef.child(pushKey);
        currentTrialRef.setValue(trail);
        //currentTrialRef.child("Trail Date").setValue(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(startDate));
        currentTrialRef.child("TimeStamp").setValue(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).format(currentTimestamp));
        dialog.dismiss();
        Toast.makeText(TrailActivity.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
    }


    /*
    * This Method Updates Trails In The Database
    * */
    private void updateTrailInDB(String trailKey, String name, String code, String moduleText, String newDate, String newTrailId) {
        DatabaseReference ctRef;
        ctRef = dRef.child(trailKey);
        ctRef.child("trailCode").setValue(code);
        ctRef.child("module").setValue(moduleText);
        ctRef.child("trailID").setValue(newTrailId);
        ctRef.child("trailName").setValue(name);
        ctRef.child("trailDate").setValue(newDate);
        //ctRef.child("Trail Date").setValue(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(newDate));
        dialog.dismiss();
        Toast.makeText(TrailActivity.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
    }


    /*
    * Checks For Correctness Of The Details Enters And Then Pushes To DB
    * */
    private void validateAndPushToDB(String name, String code, String traildate, String moduleText) {
        if (isValid(name, code, traildate)) {
            Trail newTrail = new Trail(name, code, moduleText, traildate, user.getUid());
            String trailId = newTrail.getTrailID();
            addTrailToDB(trailId, newTrail);
        }
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

                DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date oldDate = null, newDate = null;

                Trail currentTrail;
                String name = trailName.getText().toString().trim();
                String code = trailCode.getText().toString().trim();
                String moduleText = module.getText().toString().trim();
                String traildate = trailDate.getText().toString().trim();

                if(editedView != null) {
                    if((name != editedTrailName) || (code != editedTrailCode) || (moduleText != editedTrailModule) || (traildate != editedTrailDate)) {

                        try { oldDate = sourceFormat.parse(editedTrailDate);
                              newDate = sourceFormat.parse(traildate); }
                        catch (ParseException e) { e.printStackTrace(); }
                        oldTrailId = geTrailId(editedTrailCode, oldDate);
                        newTrailId = geTrailId(code, newDate);
                        App.trainer.editTrail(name, code, moduleText, traildate, oldTrailId, newTrailId, user.getUid());
                        String trailKey = App.trainer.getTrail(newTrailId).getTrailKey();
                        updateTrailInDB(trailKey, name, code, moduleText, traildate, newTrailId);
                    }
                }
                else { validateAndPushToDB(name, code, traildate, moduleText); }
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
            editedTrailName = ((EditText) editedView.findViewById(R.id.TrailNametxt)).getText().toString().trim();
            editedTrailModule = ((EditText) editedView.findViewById(R.id.Moduletxt)).getText().toString().trim();
            editedTrailCode = ((EditText) editedView.findViewById(R.id.TrailCodetxt)).getText().toString().trim();
            editedTrailDate = ((EditText) editedView.findViewById(R.id.datetxt)).getText().toString().trim();
            try { editedTrailDt = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(editedTrailDate); }
            catch (ParseException e) { e.printStackTrace(); }
            editedTrailId = geTrailId(editedTrailCode, editedTrailDt);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.homebtn:
                Intent intent = new Intent(TrailActivity.this,SelectModeActivity.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected

            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(TrailActivity.this,"Press Home Button to go back", Toast.LENGTH_SHORT).show();
    }
}

