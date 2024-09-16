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
public class EditFeedbackBBTest {

    @Rule
    public ActivityScenarioRule<EditFeedbackActivity> activityScenarioRule
            = new ActivityScenarioRule<>(EditFeedbackActivity.class);

    private static final Intent EditFeedbackActivityIntent;
    static {
        EditFeedbackActivityIntent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), EditFeedbackActivity.class);
        EditFeedbackActivityIntent.putExtra("feedbackTokenId", "temp");
    }

    @Test
    public void testCourseRatingBar() {
        onView(withId(R.id.courseRatingBar)).check(matches(isDisplayed()));
    }
    @Test
    public void testWorkloadRatingBar() {
        onView(withId(R.id.workloadRatingBar)).check(matches(isDisplayed()));
    }
    @Test
    public void testProfessorRatingBar() {
        onView(withId(R.id.professorRatingBar)).check(matches(isDisplayed()));
    }
    @Test
    public void testCommentsEditText() {
        onView(withId(R.id.commentsEditText)).check(matches(isDisplayed()));
    }
    @Test
    public void testCheckAttendanceSwitch() {
        onView(withId(R.id.checkAttendanceSwitch)).check(matches(isDisplayed()));
    }
    @Test
    public void testLateSubmissionSwitch() {
        onView(withId(R.id.lateSubmissionSwitch)).check(matches(isDisplayed()));
    }
    @Test
    public void testUpdateFeedbackButton() {
        onView(withId(R.id.updateFeedbackButton)).check(matches(isDisplayed()));
    }
}
