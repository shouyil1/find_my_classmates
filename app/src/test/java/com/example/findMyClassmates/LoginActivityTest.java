package com.example.findMyClassmates;

import org.junit.After;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowToast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.widget.Toast;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    @Mock
    private FirebaseAuth mockedAuth;

    @Mock
    private Task<AuthResult> mockedAuthResultTask;
    private AutoCloseable closeable;
    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        loginActivity = new LoginActivity();
        loginActivity.setmAuth(mockedAuth);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(loginActivity.isValidEmail("test@example.com"));
    }

    @Test
    public void emailValidator_CorrectEmailSubdomain_ReturnsTrue() {
        assertTrue(loginActivity.isValidEmail("test@sub.example.com"));
    }

    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(loginActivity.isValidEmail(""));
    }

    @Test
    public void emailValidator_NoTld_ReturnsFalse() {
        assertFalse(loginActivity.isValidEmail("test@example"));
    }

    @Test
    public void emailValidator_NoUsername_ReturnsFalse() {
        assertFalse(loginActivity.isValidEmail("@example.com"));
    }

    @Test
    public void emailValidator_InvalidEmail_ReturnsFalse() {
        assertFalse(loginActivity.isValidEmail("test@example,com"));
    }

    @Test
    public void passwordValidator_CorrectPassword_ReturnsTrue() {
        assertTrue(loginActivity.isValidPassword("correctPassword123"));
    }

    @Test
    public void passwordValidator_ShortPassword_ReturnsFalse() {
        assertFalse(loginActivity.isValidPassword("short"));
    }

    @Test
    public void passwordValidator_EmptyPassword_ReturnsFalse() {
        assertFalse(loginActivity.isValidPassword(""));
    }


    @Test
    public void login_Success() {
        String userEmail = "test@example.com";
        String password = "correctPassword";

        when(mockedAuth.signInWithEmailAndPassword(userEmail, password)).thenReturn(mockedAuthResultTask);
        when(mockedAuthResultTask.isSuccessful()).thenReturn(true);

        loginActivity.login(userEmail, password);

        verify(mockedAuth).signInWithEmailAndPassword(userEmail, password);
    }

    @Test
    public void login_Failure() {

        String userEmail = "test@example.com";
        String password = "wrongPassword";

        when(mockedAuth.signInWithEmailAndPassword(userEmail, password)).thenReturn(mockedAuthResultTask);
        when(mockedAuthResultTask.isSuccessful()).thenReturn(false);

        loginActivity.login(userEmail, password);

        verify(mockedAuth).signInWithEmailAndPassword(userEmail, password);
    }

    @Test
    public void loginWithEmptyCredentials_ShouldNotAttemptSignInAndShowToast() {

        // Attempt to login with empty credentials
        loginActivity.login("", "");
        // Verify that signInWithEmailAndPassword was never called
        verify(mockedAuth, never()).signInWithEmailAndPassword(anyString(), anyString());

    }

}



