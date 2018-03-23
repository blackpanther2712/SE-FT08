package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.models.Trail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by afaqueahmad on 23/3/18.
 */

public class TrailActivityTest {

    /*
    * This test rule enables launching of the activity
    * */
    @Rule
    public ActivityTestRule<TrailActivity> trailActivityActivityTestRule = new ActivityTestRule<TrailActivity>(TrailActivity.class);


    /*
    * Creating a reference to the activity to be tested
    * */
    private TrailActivity trailActivity = null;


    /*
    * This method runs before all the tests are run
    * */
    @Before
    public void setUp() throws Exception {
        trailActivity = trailActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch() {
        View view = trailActivity.findViewById(R.id.trail_list);
        assertNotNull(view);
    }


    /*
    * This method runs after all the tests are complete
    * */
    @After
    public void tearDown() throws Exception {
        trailActivityActivityTestRule = null;
    }

}