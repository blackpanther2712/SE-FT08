package com.ft08.trailblazelearn.fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.widget.RelativeLayout;
import android.view.View;
import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.StationActivity;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class StationFragmentTest {

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SwipeTabsActivity.class.getName(),null,false);

    @Rule
    public ActivityTestRule<StationActivity> sActivityTestRule = new ActivityTestRule<StationActivity>(StationActivity.class);

    private StationActivity stationActivity =null;

    @Before
    public void setUp() throws Exception {
        stationActivity = sActivityTestRule.getActivity();
    }

    @Test


    @After
    public void tearDown() throws Exception {
        stationActivity = null;
    }

}