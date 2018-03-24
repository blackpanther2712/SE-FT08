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
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class StationFragmentTest {

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SwipeTabsActivity.class.getName(),null,false);
    private String trailKey = "-L8GwWWec3XIregbYHw4";
    private String trailId = "20180323-Tc";

    @Rule
    public ActivityTestRule<StationActivity> sActivityTestRule = new ActivityTestRule<StationActivity>(StationActivity.class);

    private StationActivity stationActivity =null;

    @Before
    public void setUp() throws Exception {
        stationActivity = sActivityTestRule.getActivity();
    }

    @Test
    public void testFabActionButton() {
        StationFragment stationFragment = new StationFragment();
        sActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.trail_activity, stationFragment);
        onView(withId(R.id.fab)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        stationActivity = null;
    }

}