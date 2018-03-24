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
public class WelcomeActivityTest {

    /*
    * This test rule enables launching of the activity
    * */
    @Rule
    public ActivityTestRule<WelcomeActivity> welcomeActivityActivityTestRule = new ActivityTestRule<WelcomeActivity>(WelcomeActivity.class);

    /*
    * Creating a reference to the activity to be tested
    * */
    private WelcomeActivity welcomeActivity = null;

    /*
   * This method runs before all the tests are run
   * */
    @Before
    public void setUp() throws Exception {
        welcomeActivity = welcomeActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = welcomeActivity.findViewById(R.id.view_pager);
        assertNotNull(view1);

        View view2 = welcomeActivity.findViewById(R.id.layoutDots);
        assertNotNull(view2);

        View view3 = welcomeActivity.findViewById(R.id.btn_next);
        assertNotNull(view3);

        View view4 = welcomeActivity.findViewById(R.id.btn_skip);
        assertNotNull(view4);
    }

    /*
   * This method runs after all the tests are complete
   * */
    @After
    public void tearDown() throws Exception {
        welcomeActivityActivityTestRule = null;
    }

}