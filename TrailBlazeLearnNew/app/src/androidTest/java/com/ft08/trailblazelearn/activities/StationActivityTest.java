package com.ft08.trailblazelearn.activities;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by keerthanadevi on 24/3/18.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class StationActivityTest {

    public static final String trailKey = "-L8GwWWec3XIregbYHw4";
    public static final String trailid = "20180323-Tc";

    /*
     * This test rule enables launching of the activity
     * */
    @Rule
    public ActivityTestRule<StationActivity> stationActivityActivityTestRule = new ActivityTestRule<StationActivity>(StationActivity.class);

    /*
    * Creating a reference to the activity to be tested
    * */
    private StationActivity stationActivity = null;

    /*
   * This method runs before all the tests are run
   * */
    @Before
    public void setUp() throws Exception {
        stationActivity = stationActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = stationActivity.findViewById(R.id.tab_layout);
        assertNotNull(view1);

        View view2 = stationActivity.findViewById(R.id.pager);
        assertNotNull(view2);
    }


    /*
  * This method runs after all the tests are complete
  * */
    @After
    public void tearDown() throws Exception {
        stationActivityActivityTestRule = null;
    }

}