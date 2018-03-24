package com.ft08.trailblazelearn.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SelectModeActivity extends AppCompatActivity {

    private Button joinBtn, proceedBtn;
    private FirebaseAuth mAuth;
    private TextView currentUser, typeTxt;
    private DrawerLayout draw;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseUser user;
    private User users;
    private Trainer userTrainer;
    private Participant userParticipant;
    private Switch aSwitch;
    private EditText joiningTrailTxt;
    private ImageView imgtype;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = database.child("Trails");
    private String personGivenName, personEmail;
    private FirebaseAuth.AuthStateListener mListener;
    private GoogleSignInAccount acct;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        initReferences();
        navigationDrawer();
        mainBodyView();

        /*
         * Based on switch Enabled or Disable, it proceeds to respective activities
         */
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 * Enter as a Participant
                 */
                if (aSwitch.isChecked()) {
                    userParticipant = new Participant(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
                    new App(userParticipant);
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectModeActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.enter_trail_dialogbox, null);
                    joiningTrailTxt = mView.findViewById(R.id.etPassword);
                    joinBtn = mView.findViewById(R.id.joinBtn);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();

                    joinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String trailId = joiningTrailTxt.getText().toString().trim();
                            if (isValid()) {
                                if (App.trailsKeyId.containsKey(trailId)) {
                                    userParticipant.setTrailId(trailId);
                                    Intent intent = new Intent(SelectModeActivity.this, StationActivity.class);
                                    final String trailKey = App.trailsKeyId.get(trailId);
                                    intent.putExtra("trailKey", trailKey);
                                    intent.putExtra("trailId", trailId);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                } else {
                                    joiningTrailTxt.setError(getString(R.string.JoinTrailError));
                                }
                            }
                        }
                    });
                    dialog.show();
                }

                /*
                 * Enter as a Trainer
                 */
                else {
                    Intent trails = new Intent(SelectModeActivity.this, TrailActivity.class);
                    userTrainer = new Trainer(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
                    new App(userTrainer);
                    startActivity(trails);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

                /*
                 * Only authenticated User can go inside selectMode Activity
                 */
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(SelectModeActivity.this, MainActivity.class));
                }
            }
        };
    }




    /* Initializing All FireBase Instances
     * Initializing All Views in Main Activity
     */
    private void initReferences() {
        mAuth = FirebaseAuth.getInstance();
        typeTxt = (TextView) findViewById(R.id.typetxt);
        imgtype = (ImageView) findViewById(R.id.ImguserType);
        aSwitch = (Switch) findViewById(R.id.switchId);
        currentUser = (TextView) findViewById(R.id.CurrentUser);
        proceedBtn = (Button) findViewById(R.id.proceedBtn);

    }


    /* Initializing Navigation Drawer
     * Initializing All Views in Navigation Drawer
     * Populating texts and image to drawer
     */
    private void navigationDrawer() {

        acct = GoogleSignIn.getLastSignedInAccount(SelectModeActivity.this);

        if (acct != null) {
            personGivenName = acct.getGivenName();
            personEmail = acct.getEmail();
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();
        users = new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
        draw = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, draw, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        draw.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.nameTxt);
        TextView nav_email = (TextView) hView.findViewById(R.id.mailtxt);
        if (acct != null) {
            nav_user.setText(personGivenName);
            nav_email.setText(personEmail);
        } else {
            nav_user.setText(user.getDisplayName());
            nav_email.setText(user.getEmail());
        }
        ImageView photo = (ImageView) hView.findViewById(R.id.displaypic);
        Glide.with(photo.getContext())
                .load(user.getPhotoUrl())
                .into(photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.logoutId) {
                    new AlertDialog.Builder(SelectModeActivity.this)
                            .setMessage(R.string.WARNING_MSG)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sendToLogin();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

                return true;
            }
        });
    }




  /* Welcome text with name
   * The body of the activity with texts,Images and Buttons
   */

    private void mainBodyView() {
        if(acct!=null) {
            currentUser.setText(getString(R.string.Hello_Main) + " " + personGivenName + "!!");
        }
        else{
            currentUser.setText(getString(R.string.Hello_Main) + " " + user.getDisplayName() + "!!");
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgtype.setImageResource(R.drawable.participant);
                    typeTxt.setText(R.string.AS_A_PARTICIPANT);

                } else {
                    imgtype.setImageResource(R.drawable.trainer);
                    typeTxt.setText(R.string.AS_A_TRAINER);
                }
            }
        });
    }



    /* Sends to Main activity on Click of Logout in drawer
     * Logouts from application and signing out of fireBase
     */

    private void sendToLogin() {

        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                         /*signout firebase
                         */

                        FirebaseAuth.getInstance().signOut();
                        Intent setupIntent = new Intent(getBaseContext(), MainActivity.class);
                        Toast.makeText(getBaseContext(), R.string.logOut, Toast.LENGTH_LONG).show();
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }


    /*
     * Validation of Entering TrailCode
     */
    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(joiningTrailTxt.getText().toString().trim())) {
            joiningTrailTxt.setError(getString(R.string.enterTrailMsg));
            isValid = false;
        }
        return isValid;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


}
