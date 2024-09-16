//package com.example.findMyClassmates;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.swipeLeft;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.Espresso.closeSoftKeyboard;
//
//import static org.hamcrest.Matchers.allOf;
//
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.UiController;
//import androidx.test.espresso.ViewAction;
//import android.view.View;
//import android.view.MotionEvent;
//import android.view.InputDevice;
//
//import org.hamcrest.Matcher;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class FindBBTest {
//
//    @Test
//    public void testFindButtonNavigation() {
//        // Start the LoginActivity and perform login
//        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
//            onView(withId(R.id.editTextUsername)).perform(typeText("testing@usc.edu"));
//            onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
//            onView(withId(R.id.buttonLogin)).perform(click());
//
//            // Add delays or use IdlingResource to wait for the main activity to be ready
//
//            // Navigate to the second tab by clicking on its coordinates
//            onView(allOf(withId(R.id.tabLayout), isDisplayed())).perform(swipeLeft()); // Example coordinates
//
//            // Open the first list item by clicking on its coordinates
//            onView(withId(R.id.enrollments)).perform(clickCoordinates(100, 300)); // Example coordinates
//
//            // Tap the Find button using its coordinates
//            onView(withId(R.id.findClassmatesButton)).perform(clickCoordinates(900, 300)); // Example coordinates
//        }
//    }
//
//    // A custom ViewAction to tap on specific screen coordinates
//    private static ViewAction clickCoordinates(final float x, final float y) {
//        return new ViewAction() {
//            @Override
//            public Matcher<View> getConstraints() {
//                return isDisplayed();
//            }
//
//            @Override
//            public String getDescription() {
//                return "tap at coordinates (" + x + ", " + y + ")";
//            }
//
//            @Override
//            public void perform(UiController uiController, View view) {
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                float[] coordinates = new float[]{location[0] + x, location[1] + y};
//                float[] precision = new float[]{1f, 1f};
//
//                MotionEvent downEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(),
//                        MotionEvent.ACTION_DOWN, coordinates[0], coordinates[1], 0);
//                MotionEvent upEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(),
//                        MotionEvent.ACTION_UP, coordinates[0], coordinates[1], 0);
//
//                view.dispatchTouchEvent(downEvent);
//                view.dispatchTouchEvent(upEvent);
//
//                downEvent.recycle();
//                upEvent.recycle();
//            }
//        };
//    }
//}
