package com.ft08.trailblazelearn.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ft08.trailblazelearn.fragments.FragmentA;
import com.ft08.trailblazelearn.fragments.FragmentB;
import com.ft08.trailblazelearn.fragments.FragmentC;

/**
 * Created by afaqueahmad on 18/3/18.
 */

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
                fragment = new FragmentA();
                break;
            case 1:
                fragment = new FragmentB();
                break;
            case 2:
                fragment = new FragmentC();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}