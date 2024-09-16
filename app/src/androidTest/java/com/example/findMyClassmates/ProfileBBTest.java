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

public class ProfileBBTest {
    private String userEmail = "BBTest1@example.com"; // Example email

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testProfileDisplay1() {
        // Create an intent with the user email
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(2);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.editProfileButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testProfileDisplay2() {
        // Create an intent with the user email
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(2);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.userName)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testProfileDisplay3() {
        // Create an intent with the user email
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(2);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.id)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testProfileDisplay4() {
        // Create an intent with the user email
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(2);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testProfileDisplay5() {
        // Create an intent with the user email
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(2);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.saveButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testProfileDisplay6() {
        // Create an intent with the user email
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        String userEmail = "BBTest1@example.com"; // Example email
        intent.putExtra("userEmail", userEmail);

        // Launch the activity with the intent
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Navigate to the Enrollment tab
                activity.navigateToTab(2);
            });

            // Adding a delay to allow the ViewPager to complete the transaction
            try {
                Thread.sleep(1000); // 1000 milliseconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Add any assertions or interactions here
            onView(withId(R.id.role)).check(matches(isDisplayed()));
        }
    }

}
