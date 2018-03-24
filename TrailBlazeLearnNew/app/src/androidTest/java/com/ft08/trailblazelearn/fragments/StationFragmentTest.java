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

/**
 * Created by keerthanadevi on 24/3/18.
 */
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
    public void testLaunch(){
        String name = "empty";
        RelativeLayout rContainer = (RelativeLayout) stationActivity.findViewById(R.id.test_container);
        assertNotNull(rContainer);
        StationFragment stationFragment = new StationFragment();
        //stationActivity.getFragmentManager().beginTransaction().add(rContainer.getId(),stationFragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = stationFragment.getView().findViewById(R.id.trail_list);
        assertNotNull(view);
       Espresso.onView(withId(R.id.empty_value)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

//        assertNotNull(stationActivity.findViewById(R.id.fab));
//        Espresso.onView(withId(R.id.fab)).perform(click());
//        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
//        assertNotNull(nextActivity);
//        nextActivity.finish();

    }

    @After
    public void tearDown() throws Exception {
        stationActivity = null;
    }

}