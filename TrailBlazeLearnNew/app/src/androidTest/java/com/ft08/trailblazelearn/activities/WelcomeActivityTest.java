package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class WelcomeActivityTest {

    @Rule
    public ActivityTestRule<WelcomeActivity> welcomeActivityActivityTestRule = new ActivityTestRule<WelcomeActivity>(WelcomeActivity.class);

    private WelcomeActivity welcomeActivity = null;

    @Before
    public void setUp() throws Exception {
        welcomeActivity = welcomeActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = welcomeActivity.findViewById(R.id.view_pager);
        View view2 = welcomeActivity.findViewById(R.id.layoutDots);
        View view3 = welcomeActivity.findViewById(R.id.btn_next);
        View view4 = welcomeActivity.findViewById(R.id.btn_skip);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
    }

    @After
    public void tearDown() throws Exception {
        welcomeActivityActivityTestRule = null;
    }

}