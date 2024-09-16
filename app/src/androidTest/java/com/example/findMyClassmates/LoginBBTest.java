package com.example.findMyClassmates;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginBBTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginSuccess() {
        // Replace these with valid credentials
        String userEmail = "testing@usc.edu";
        String password = "123456";

        onView(withId(R.id.editTextUsername)).perform(typeText(userEmail));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.buttonLogin)).perform(click());

        // Add a delay to wait for the response - not recommended, better use IdlingResource
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assuming MainActivity is opened upon successful login
        onView(withId(R.id.viewPager)) // Replace with a view from MainActivity
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        String invalidEmail = "invalid@example.com";
        String invalidPassword = "wrongPass";

        onView(withId(R.id.editTextUsername)).perform(typeText(invalidEmail));
        onView(withId(R.id.editTextPassword)).perform(typeText(invalidPassword));
        onView(withId(R.id.buttonLogin)).perform(click());

        // Add a delay or use IdlingResource
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.editTextUsername)).check(matches(isDisplayed()));
    }
}
