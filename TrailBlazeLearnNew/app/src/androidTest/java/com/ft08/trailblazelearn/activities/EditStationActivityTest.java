package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by keerthanadevi on 24/3/18.
 */
public class EditStationActivityTest {

    /*
    * This test rule enables launching of the activity
    * */
    @Rule
    public ActivityTestRule<EditStationActivity> editStationActivityActivityTestRule = new ActivityTestRule<EditStationActivity>(EditStationActivity.class);

    /*
    * Creating a reference to the activity to be tested
    * */
    private EditStationActivity editStationActivity = null;

    /*
   * This method runs before all the tests are run
   * */
    @Before
    public void setUp() throws Exception {
        editStationActivity = editStationActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = editStationActivity.findViewById(R.id.dialogStation);
        assertNotNull(view1);

        View view2 = editStationActivity.findViewById(R.id.stationName);
        assertNotNull(view2);

        View view3 = editStationActivity.findViewById(R.id.gps);
        assertNotNull(view3);

        View view4 = editStationActivity.findViewById(R.id.instructions);
        assertNotNull(view4);

        View view5 = editStationActivity.findViewById(R.id.CreateBtn);
        assertNotNull(view5);
    }

    /*
   * This method runs after all the tests are complete
   * */
    @After
    public void tearDown() throws Exception {
        editStationActivityActivityTestRule = null;
    }

}