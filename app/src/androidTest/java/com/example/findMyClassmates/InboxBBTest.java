package com.example.findMyClassmates;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;

import android.view.View;

@RunWith(AndroidJUnit4.class)
public class InboxBBTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        onView(withId(R.id.editTextUsername)).perform(typeText("testing@usc.edu"));
        onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
        closeSoftKeyboard();


        onView(withId(R.id.buttonLogin)).perform(click());

        try {
            Thread.sleep(2000); // Wait for 2 seconds (adjust as necessary)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testingInboxButton() {
        onView(withId(R.id.inboxButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testingTransitionToInbox() {
        onView(withId(R.id.inboxButton)).perform(click());
        onView(withId(R.id.inboxTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testingTransitionToChatRoom() {
        onView(withId(R.id.inboxButton)).perform(click());
        onView(withId(R.id.chatroomRecyclerView))
                .perform(waitForView(withId(R.id.chatroomName), 5000))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    public static ViewAction waitForView(final Matcher<View> viewMatcher, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewMatcher + "> during " + millis + " millis.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                long startTime = System.currentTimeMillis();
                long endTime = startTime + millis;
                final Matcher<View> constraintMatcher = withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE); // View must be visible

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(child) && constraintMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .build();
            }
        };
    }

}
