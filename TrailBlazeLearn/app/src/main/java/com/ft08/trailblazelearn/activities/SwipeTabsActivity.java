package com.ft08.trailblazelearn.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ft08.trailblazelearn.R;

/**
 * Created by afaqueahmad on 17/3/18.
 */

public class SwipeTabsActivity  extends FragmentActivity implements ActionBar.TabListener {

    ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_tabs);

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab details = actionBar.newTab();
        details.setText("Details");
        details.setTabListener(this);

        ActionBar.Tab  discussions = actionBar.newTab();
        discussions.setText("Discussions");
        discussions.setTabListener(this);

        ActionBar.Tab contributed_items = actionBar.newTab();
        contributed_items.setText("Contributed Items");
        contributed_items.setTabListener(this);

        actionBar.addTab(details);
        actionBar.addTab(discussions);
        actionBar.addTab(contributed_items);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.d("APP", "onTabSelected at position: " + tab.getPosition() + " name " + tab.getText());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.d("APP", "onTabUnselected at position: " + tab.getPosition() + " name " + tab.getText());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.d("APP", "onTabReselected at position: " + tab.getPosition() + " name " + tab.getText());
    }
}
