package com.ft08.trailblazelearn.activities;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.DatePicker;

import com.ft08.trailblazelearn.R;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class TrailActivityTest {

    @Rule
    public ActivityTestRule<TrailActivity> trailActivityActivityTestRule = new ActivityTestRule<TrailActivity>(TrailActivity.class);

    private TrailActivity trailActivity = null;
    final private String DIALOG_TITLE = "Add Trail";
    final private String TRAILNAME = "Trail Name";
    final private String TRAILCODE = "Trail Code";
    final private String MODULE = "Module";
    final private String DATE = "Date";
    final private String INPUT_TRAILNAME = "GotoNUS";
    final private String INPUT_TRAILCODE = "GTNUS";
    final private String INPUT_MODULE = "First Module";

    @Before
    public void setUp() throws Exception {
        trailActivity = trailActivityActivityTestRule.getActivity();
    }

    @Test
    public void testViewLaunches() {
        View view = trailActivity.findViewById(R.id.trail_list);
        assertNotNull(view);
    }

    @Test
    public void testAddTrail() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.add_trail_dialog_box)).check(matches(isDisplayed()));
        onView(withId(R.id.dialogTitle)).check(matches(withText(DIALOG_TITLE)));
        onView(withId(R.id.trailname)).check(matches(withText(TRAILNAME)));
        onView(withId(R.id.trailcode)).check(matches(withText(TRAILCODE)));
        onView(withId(R.id.mod)).check(matches(withText(MODULE)));
        onView(withId(R.id.date)).check(matches(withText(DATE)));
        onView(withId(R.id.TrailNametxt)).perform(typeText(INPUT_TRAILNAME), closeSoftKeyboard());
        onView(withId(R.id.TrailCodetxt)).perform(typeText(INPUT_TRAILCODE), closeSoftKeyboard());
        onView(withId(R.id.Moduletxt)).perform(typeText(INPUT_MODULE), closeSoftKeyboard());
        onView(withId(R.id.CreateBtn)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        trailActivityActivityTestRule = null;
    }
}