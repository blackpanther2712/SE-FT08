package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = mainActivity.findViewById(R.id.trailBlazeImg);
        View view2 = mainActivity.findViewById(R.id.facebookBtn);
        View view3 = mainActivity.findViewById(R.id.GoogleBtn);
        View view4 = mainActivity.findViewById(R.id.textView);
        View view5 = mainActivity.findViewById(R.id.textView2);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
    }

    @After
    public void tearDown() throws Exception {
        mainActivityActivityTestRule = null;
    }

}