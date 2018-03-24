package com.ft08.trailblazelearn.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ft08.trailblazelearn.fragments.StationDetailsFragment;
import com.ft08.trailblazelearn.fragments.StationDiscussionFragment;
import com.ft08.trailblazelearn.fragments.StationContributedItemFragment;

public class SwipeTabAdapter extends FragmentPagerAdapter {

    private final int numberOfTabs = 3;

    public SwipeTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new StationDetailsFragment();
                break;
            case 1:
                fragment = new StationDiscussionFragment();
                break;
            case 2:
                fragment = new StationContributedItemFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}