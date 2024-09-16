package com.example.findMyClassmates;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


import android.content.Intent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FindClassMatesBBTest {

    // Rule to start FindActivity
    @Rule
    public ActivityScenarioRule<FindActivity> activityScenarioRule
            = new ActivityScenarioRule<>(FindActivity.class);

    // Intent to start FindActivity with required extras
    private static final Intent findActivityIntent;
    static {
        findActivityIntent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), FindActivity.class);
        findActivityIntent.putExtra("CLASS_NAME", "Software Engineering");
    }


    @Test
    public void testBackButtonNavigation() {
        onView(withId(R.id.classMatesTitle)).check(matches(isDisplayed()));
    }
}
