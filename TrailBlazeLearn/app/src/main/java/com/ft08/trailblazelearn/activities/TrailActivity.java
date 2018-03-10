package com.ft08.trailblazelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.adapters.TrailAdapter;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;


public class TrailActivity extends AppCompatActivity {

    private TextView trailEmpty;
    private TrailAdapter trailAdapter;
    private FirebaseUser userref;
    private Trainer user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail);

        user=(Trainer)getIntent().getSerializableExtra("TrainerObj");
        userref = FirebaseAuth.getInstance().getCurrentUser();
        ListView trailList = (ListView) findViewById(R.id.trail_list);
        trailEmpty = (TextView) findViewById(R.id.empty_value);
        trailAdapter = new TrailAdapter(this,user);
        trailList.setAdapter(trailAdapter);


        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent=new Intent(TrailActivity.this, AddTrailActivity.class);
                intent.putExtra("Trainerobj",(Serializable) user);
                startActivity(intent);
            }
        });

        floatingActionButton.setVisibility(getIntent().getSerializableExtra("TrainerObj")instanceof Trainer ? View.VISIBLE : View.GONE);



    }

    @Override public void onResume() {
        super.onResume();
        trailAdapter.refreshTrails();
        trailEmpty.setVisibility(trailAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }
    @Override public void onStop() {
        super.onStop();
        trailAdapter.refreshTrails();
        trailEmpty.setVisibility(trailAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }
}

