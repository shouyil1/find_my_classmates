package com.example.findMyClassmates;

import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public final class ClickCoordinatesAction implements ViewAction {
    private final float x;
    private final float y;

    public ClickCoordinatesAction(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isDisplayed(); // This custom action will only work on views that are displayed.
    }

    @Override
    public String getDescription() {
        return "click at coordinates (" + x + ", " + y + ")";
    }

    @Override
    public void perform(UiController uiController, View view) {
        int[] screenPos = new int[2];
        view.getLocationOnScreen(screenPos);

        // Offset coordinates by view location
        float screenX = screenPos[0] + x;
        float screenY = screenPos[1] + y;

        // Send down and up events to perform the click action
        MotionEvent downEvent = MotionEvent.obtain(
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                MotionEvent.ACTION_DOWN,
                screenX,
                screenY,
                InputDevice.SOURCE_TOUCHSCREEN
        );
        view.dispatchTouchEvent(downEvent);
        uiController.loopMainThreadForAtLeast(50);

        MotionEvent upEvent = MotionEvent.obtain(
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                MotionEvent.ACTION_UP,
                screenX,
                screenY,
                InputDevice.SOURCE_TOUCHSCREEN
        );
        view.dispatchTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
