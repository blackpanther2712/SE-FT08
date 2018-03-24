package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelectModeActivityTest {

    @Rule
    public ActivityTestRule<SelectModeActivity> selectModeActivityActivityTestRule = new ActivityTestRule<SelectModeActivity>(SelectModeActivity.class);

    private SelectModeActivity selectModeActivity = null;

    @Before
    public void setUp() throws Exception {
        selectModeActivity = selectModeActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = selectModeActivity.findViewById(R.id.CurrentUser);
        View view2 = selectModeActivity.findViewById(R.id.ImguserType);
        View view3 = selectModeActivity.findViewById(R.id.proceedBtn);
        View view4 = selectModeActivity.findViewById(R.id.switchId);
        View view5 = selectModeActivity.findViewById(R.id.typetxt);
        View view6 = selectModeActivity.findViewById(R.id.navigation);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
    }

    @After
    public void tearDown() throws Exception {
        selectModeActivityActivityTestRule = null;
    }
}