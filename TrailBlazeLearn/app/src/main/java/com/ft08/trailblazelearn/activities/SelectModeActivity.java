package com.ft08.trailblazelearn.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.models.Trainer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class SelectModeActivity extends AppCompatActivity {

    private Button logoutBtn;
    private Button trainer;
    private Button participant;
    private FirebaseAuth mAuth;
    private TextView currenUser;


    private FirebaseUser user;


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



        currenUser = (TextView)findViewById(R.id.CurrentUser);
        currenUser.setText("Welcome"+" "+user.getDisplayName()+"!!");

        trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trails = new Intent(SelectModeActivity.this,TrailActivity.class);
                Trainer trainer1 =new Trainer(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
                startActivity(trails);

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
                mAuth.signOut();
            }
        });
    }
}
