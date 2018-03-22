package com.ft08.trailblazelearn.activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class SelectModeActivity extends AppCompatActivity {

    private Button joinBtn,proceedBtn;
    private FirebaseAuth mAuth;
    private TextView currentUser,typeTxt;

    private DrawerLayout draw;
    private ActionBarDrawerToggle abdt;



    private FirebaseUser user;
    private User users;
    private Trainer userTrainer;
    private Participant userParticipant;
    private Switch aSwitch;
    private EditText joiningTrailTxt;
    private ImageView imgtype;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef= database.child("Trails");

    private String personGivenName,personEmail;


    private FirebaseAuth.AuthStateListener mListener;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        mAuth = FirebaseAuth.getInstance();
        typeTxt=(TextView)findViewById(R.id.typetxt);
        imgtype = (ImageView)findViewById(R.id.ImguserType);
        aSwitch = (Switch)findViewById(R.id.switchId);
        currentUser= (TextView)findViewById(R.id.CurrentUser);
        proceedBtn = (Button)findViewById(R.id.proceedBtn);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SelectModeActivity.this);

        if (acct != null) {
            personGivenName = acct.getGivenName();
            personEmail = acct.getEmail();
        }




        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();
        users = new User(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());


        draw = (DrawerLayout)findViewById(R.id.drawerLayout);
        abdt = new ActionBarDrawerToggle(this,draw,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        draw.addDrawerListener(abdt);
        abdt.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nameTxt);
        TextView nav_email = (TextView)hView.findViewById(R.id.mailtxt);
        if(acct!=null) {
            nav_user.setText(personGivenName);
            nav_email.setText(personEmail);
        }
        else{
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
                if(id == R.id.logoutId) {
                    new AlertDialog.Builder(SelectModeActivity.this)
                            .setMessage("Are you sure you want to Logout?")
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



        currentUser.setText("Hello"+" "+user.getDisplayName()+"!!");





        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgtype.setImageResource(R.drawable.participant);
                    typeTxt.setText("I'm a Participant");

                }
                else{
                    imgtype.setImageResource(R.drawable.trainer);
                    typeTxt.setText("I'm a Trainer");
                }
            }
        });




        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    userParticipant = new Participant(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
                    new App(userParticipant);
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectModeActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.enter_trail_dialogbox, null);
                    joiningTrailTxt =  mView.findViewById(R.id.etPassword);
                    joinBtn =  mView.findViewById(R.id.joinBtn);
                    mBuilder.setView(mView);
                    final  AlertDialog dialog = mBuilder.create();

                    joinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String traildId = joiningTrailTxt.getText().toString().trim();
                            if(isValid()) {
                                if(User.trailsKeyId.containsKey(traildId)) {
                                    userParticipant.setTrailId(traildId);
                                    Intent intent = new Intent(SelectModeActivity.this, StationActivity.class);
                                    final String trailKey = User.trailsKeyId.get(traildId);
                                    intent.putExtra("trailKey", trailKey);
                                    startActivity(intent);
                                }else{
                                    joiningTrailTxt.setError("Please enter correct and existing TrailID to join");
                                }
                            }

                        }
                    });
                    dialog.show();

                }
                else{
                    Intent trails = new Intent(SelectModeActivity.this,TrailActivity.class);
                    userTrainer =new Trainer(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
                    new App(userTrainer);
                    startActivity(trails);


                }
            }
        });
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(SelectModeActivity.this,MainActivity.class) );
                }
            }
        };
    }



  /*  @Override
        public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/



    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.logoutBtn:
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to Logout?")
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
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
            // action with ID action_settings was selected

            default:
                break;
        }
        return true;
    }
*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void sendToLogin() { //funtion
        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getBaseContext(),MainActivity.class);
                        Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }

    private boolean isValid () {
        boolean isValid = true;
        if (TextUtils.isEmpty(joiningTrailTxt.getText().toString().trim())) {
            joiningTrailTxt.setError("Please fill in TrailID");
            isValid = false;
        }
        return isValid;
    }




}
