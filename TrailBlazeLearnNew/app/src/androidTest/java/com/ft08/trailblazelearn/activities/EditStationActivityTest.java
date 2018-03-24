package com.ft08.trailblazelearn.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.ft08.trailblazelearn.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditStationActivityTest {

    @Rule
    public ActivityTestRule<EditStationActivity> editStationActivityActivityTestRule = new ActivityTestRule<EditStationActivity>(EditStationActivity.class);

    private EditStationActivity editStationActivity = null;

    @Before
    public void setUp() throws Exception {
        editStationActivity = editStationActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewsRef() {
        View view1 = editStationActivity.findViewById(R.id.dialogStation);
        View view2 = editStationActivity.findViewById(R.id.stationName);
        View view3 = editStationActivity.findViewById(R.id.gps);
        View view4 = editStationActivity.findViewById(R.id.instructions);
        View view5 = editStationActivity.findViewById(R.id.CreateBtn);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
    }

    @After
    public void tearDown() throws Exception {
        editStationActivityActivityTestRule = null;
    }

}