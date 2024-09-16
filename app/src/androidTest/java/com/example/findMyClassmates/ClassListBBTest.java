package com.example.findMyClassmates;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class ClassListBBTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testClassListDisplay() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(0);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.departments)).check(matches(isDisplayed()));
        }
    }
}
