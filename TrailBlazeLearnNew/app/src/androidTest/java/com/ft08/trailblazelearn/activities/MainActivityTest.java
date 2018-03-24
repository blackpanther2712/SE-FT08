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
public class MainActivityTest {

    /*
    * This test rule enables launching of the activity
    * */
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    /*
    * Creating a reference to the activity to be tested
    * */
    private MainActivity mainActivity = null;

    /*
   * This method runs before all the tests are run
   * */
    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = mainActivity.findViewById(R.id.trailBlazeImg);
        assertNotNull(view1);

        View view2 = mainActivity.findViewById(R.id.facebookBtn);
        assertNotNull(view2);

        View view3 = mainActivity.findViewById(R.id.GoogleBtn);
        assertNotNull(view3);

        View view4 = mainActivity.findViewById(R.id.textView);
        assertNotNull(view4);

        View view5 = mainActivity.findViewById(R.id.textView2);
        assertNotNull(view5);
    }

    /*
   * This method runs after all the tests are complete
   * */
    @After
    public void tearDown() throws Exception {
        mainActivityActivityTestRule = null;
    }

}