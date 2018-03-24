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
public class SelectModeActivityTest {

    /*
    * This test rule enables launching of the activity
    * */
    @Rule
    public ActivityTestRule<SelectModeActivity> selectModeActivityActivityTestRule = new ActivityTestRule<SelectModeActivity>(SelectModeActivity.class);

    /*
    * Creating a reference to the activity to be tested
    * */
    private SelectModeActivity selectModeActivity = null;

    /*
   * This method runs before all the tests are run
   * */
    @Before
    public void setUp() throws Exception {
        selectModeActivity = selectModeActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = selectModeActivity.findViewById(R.id.CurrentUser);
        assertNotNull(view1);

        View view2 = selectModeActivity.findViewById(R.id.ImguserType);
        assertNotNull(view2);

        View view3 = selectModeActivity.findViewById(R.id.proceedBtn);
        assertNotNull(view3);

        View view4 = selectModeActivity.findViewById(R.id.switchId);
        assertNotNull(view4);

        View view5 = selectModeActivity.findViewById(R.id.typetxt);
        assertNotNull(view5);

        View view6 = selectModeActivity.findViewById(R.id.navigation);
        assertNotNull(view6);
    }

    /*
  * This method runs after all the tests are complete
  * */
    @After
    public void tearDown() throws Exception {
        selectModeActivityActivityTestRule = null;
    }

}