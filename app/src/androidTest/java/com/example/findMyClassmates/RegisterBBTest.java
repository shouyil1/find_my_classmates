package com.example.findMyClassmates;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterBBTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule = new ActivityScenarioRule<>(RegisterActivity.class);


    @Test
    public void testRegisterSuccess() {
        // Use valid, but not previously used data for registration
        String email = "BBTest" + System.currentTimeMillis() + "@example.com";
        String name = "BB Test User" + System.currentTimeMillis();
        String password = "123456";
        String confirmPassword = "123456";

        // Type into the fields and click on the register button
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        onView(withId(R.id.editTextName)).perform(typeText(name));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.viewPager)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationFailure() {
        // Use invalid data for registration
        String email = "invalid email";
        String name = "User";
        String password = "123456";
        String confirmPassword = "123456";

        // Type into the fields and click on the register button
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        onView(withId(R.id.editTextName)).perform(typeText(name));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());

        // Verify that a toast with the error message is shown or the register button is still displayed
        // Depending on how your UI handles errors, you might want to check for a specific error message or the presence of the register button
        onView(withId(R.id.buttonRegister)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterInvalidPassword() {
        // Use invalid data for registration
        String email = "tommy@usc.edu";
        String name = "User";
        String password = "123";
        String confirmPassword = "123";

        // Type into the fields and click on the register button
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        onView(withId(R.id.editTextName)).perform(typeText(name));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());

        onView(withId(R.id.buttonRegister)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterDifferentPassword() {
        // Use invalid data for registration
        String email = "tommy@usc.edu";
        String name = "User";
        String password = "123";
        String confirmPassword = "321";

        // Type into the fields and click on the register button
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        onView(withId(R.id.editTextName)).perform(typeText(name));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());

        onView(withId(R.id.buttonRegister)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterEmptyEmails() {
        // Use invalid data for registration
        String email = "";
        String name = "User";
        String password = "123456";
        String confirmPassword = "123456";

        // Type into the fields and click on the register button
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        onView(withId(R.id.editTextName)).perform(typeText(name));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());

        onView(withId(R.id.buttonRegister)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterEmptyPasswords() {
        // Use invalid data for registration
        String email = "test@usc.edu";
        String name = "User";
        String password = "";
        String confirmPassword = "";

        // Type into the fields and click on the register button
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        onView(withId(R.id.editTextName)).perform(typeText(name));
        onView(withId(R.id.editTextPassword)).perform(typeText(password));
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());

        onView(withId(R.id.buttonRegister)).check(matches(isDisplayed()));
    }
}
