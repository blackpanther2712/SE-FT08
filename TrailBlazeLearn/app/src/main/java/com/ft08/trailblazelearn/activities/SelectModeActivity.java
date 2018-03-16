package com.ft08.trailblazelearn.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class SelectModeActivity extends AppCompatActivity {

    private Button logoutBtn,joinBtn;
    private Button trainer;
    private Button participant;
    private FirebaseAuth mAuth;
    private TextView currentUser;


    private FirebaseUser user;
    private User users;
    private Trainer userTrainer;
    private Participant userParticipant;

    private EditText joiningTrailTxt;

    private DatabaseReference myRef;


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
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        trainer =(Button)findViewById(R.id.TrainerBtn);
        participant =(Button)findViewById(R.id.ParticipantBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();
        users = new User(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());


        currentUser= (TextView)findViewById(R.id.CurrentUser);
        currentUser.setText("Welcome"+" "+user.getDisplayName()+"!!");

        trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trails = new Intent(SelectModeActivity.this,TrailActivity.class);
                userTrainer =new Trainer(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
                new App(userTrainer);
                startActivity(trails);

            }
        });

//        participant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent parti = new Intent(this,ParticipantActivity.class);
//                userParticipant = new Participant(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
//                new App(userParticipant);
//                startActivity(parti);
//
//            }
//        });

        participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userParticipant = new Participant(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
                new App(userParticipant);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectModeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.enter_trail_dialogbox, null);


                joiningTrailTxt = (EditText) mView.findViewById(R.id.etPassword);

                joinBtn = (Button) mView.findViewById(R.id.joinBtn);
                mBuilder.setView(mView);
                final  AlertDialog dialog = mBuilder.create();

                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String traildId = joiningTrailTxt.getText().toString().trim();
                        userParticipant.setTrailId(traildId);
                        Intent intent = new Intent(SelectModeActivity.this, StationActivity.class);
                        intent.putExtra("trailId",userParticipant.getTrailId());
                        startActivity(intent);


                   }
                });
                dialog.show();

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
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendToLogin();
            }
        });
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
}
