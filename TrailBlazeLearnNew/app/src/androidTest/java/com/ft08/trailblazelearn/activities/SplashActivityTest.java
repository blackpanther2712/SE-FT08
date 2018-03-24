package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashActivityActivityTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    private SplashActivity splashActivity = null;

    @Before
    public void setUp() throws Exception {
        splashActivity = splashActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view = splashActivity.findViewById(R.id.splashImg2);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        splashActivityActivityTestRule = null;
    }

}