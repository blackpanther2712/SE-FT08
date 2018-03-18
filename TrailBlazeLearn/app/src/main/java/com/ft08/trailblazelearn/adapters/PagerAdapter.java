package com.ft08.trailblazelearn.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.ft08.trailblazelearn.fragments.StationFragment;
import com.ft08.trailblazelearn.fragments.LocationsFragment;

/**
 * Created by keerthanadevi on 13/3/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StationFragment stationFragment = new StationFragment();
                return stationFragment;
            default:
                LocationsFragment mapFragment = new LocationsFragment();
                return mapFragment;

        }
    }

    @Override public int getCount() {
        return numOfTabs;
    }

}
