package com.ft08.trailblazelearn.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ft08.trailblazelearn.adapters.PagerAdapter;
import com.ft08.trailblazelearn.fragments.StationFragment;
import com.google.firebase.database.ValueEventListener;

public class StationActivity extends AppCompatActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        Bundle bundle = getIntent().getExtras();
        final String trailID = bundle.getString("trailId");

        StationFragment.newInstance(trailID);

        this.viewPager= (ViewPager) findViewById(R.id.pager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Stations"));
        tabLayout.addTab(tabLayout.newTab().setText("Locations"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


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
                Intent intent = new Intent(StationActivity.this,SelectModeActivity.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0);
    }
}
