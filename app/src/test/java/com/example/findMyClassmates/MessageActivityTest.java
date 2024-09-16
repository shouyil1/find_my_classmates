package com.example.findMyClassmates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.firebase.firestore.FirebaseFirestore;

@RunWith(RobolectricTestRunner.class)
public class MessageActivityTest {

    @Mock
    FirebaseFirestore mockedFirestore;

    private MessageActivity messageActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        messageActivity = new MessageActivity();
        messageActivity.setDB(mockedFirestore);
    }

    @Test
    public void validateInput_WhenInputIsValid() {
        assertTrue(messageActivity.validateInput("Hello World"));
    }

    @Test
    public void validateInput_WhenInputIsInvalid() {
        assertFalse(messageActivity.validateInput(""));
        assertFalse(messageActivity.validateInput("   "));
        assertFalse(messageActivity.validateInput(null));
    }
}
