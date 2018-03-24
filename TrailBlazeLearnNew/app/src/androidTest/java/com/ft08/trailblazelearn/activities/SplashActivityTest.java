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
public class SplashActivityTest {
    /*
    * This test rule enables launching of the activity
    * */
    @Rule
    public ActivityTestRule<SplashActivity> splashActivityActivityTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    /*
    * Creating a reference to the activity to be tested
    * */
    private SplashActivity splashActivity = null;

    /*
   * This method runs before all the tests are run
   * */
    @Before
    public void setUp() throws Exception {
        splashActivity = splashActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view = splashActivity.findViewById(R.id.splashImg2);
        assertNotNull(view);
    }

    /*
   * This method runs after all the tests are complete
   * */
    @After
    public void tearDown() throws Exception {
        splashActivityActivityTestRule = null;
    }

    }