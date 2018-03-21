package com.ft08.trailblazelearn.activities;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.adapters.PagerAdapter;
import com.ft08.trailblazelearn.adapters.SwipeTabAdapter;
import com.ft08.trailblazelearn.fragments.FragmentA;
import com.ft08.trailblazelearn.fragments.FragmentB;
import com.ft08.trailblazelearn.fragments.FragmentC;

public class SwipeTabsActivity  extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static String calledTrailId;
    private static String calledStationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_tabs);

        viewPager = (ViewPager) findViewById(R.id.swipePager);
        viewPager.setAdapter(new SwipeTabAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_swipe);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Discussions"));
        tabLayout.addTab(tabLayout.newTab().setText("Contributions"));
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Bundle savedDataFromPreviousActivity = getIntent().getExtras();
        setCalledStationId((String) savedDataFromPreviousActivity.get("stationId"));
        setCalledTrailId((String) savedDataFromPreviousActivity.get("trailId"));
    }

    public static String getCalledTrailId() { return calledTrailId; }

    public static void setCalledTrailId(String calledTrailId) { SwipeTabsActivity.calledTrailId = calledTrailId; }

    public static String getCalledStationId() { return calledStationId; }

    public static void setCalledStationId(String calledStationId) { SwipeTabsActivity.calledStationId = calledStationId; }

    @Override
    public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition()); }
    public void onTabUnselected(TabLayout.Tab tab) {}
    public void onTabReselected(TabLayout.Tab tab) {}
    /*@Override
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
                Intent intent = new Intent(SwipeTabsActivity.this,SelectModeActivity.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected

            default:
                break;
        }

        return true;
    }*/
}
