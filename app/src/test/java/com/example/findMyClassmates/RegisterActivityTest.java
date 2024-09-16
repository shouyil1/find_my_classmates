package com.example.findMyClassmates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.*;

import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest {

    @Mock
    private FirebaseAuth mockAuth;
    @Mock
    private FirebaseFirestore mockDb;
    @Mock
    private EditText mockEmailEditText;
    @Mock
    private EditText mockPasswordEditText;
    @Mock
    private EditText mockConfirmPasswordEditText;
    @Mock
    private Button mockRegisterButton;
    @Mock
    private Task<AuthResult> mockAuthResultTask;

    private RegisterActivity registerActivity;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerActivity = new RegisterActivity();

        // Dependency Injection
        registerActivity.setFirebaseAuth(mockAuth);
        registerActivity.setFirebaseFirestore(mockDb);
        registerActivity.setEditTextEmail(mockEmailEditText);
        registerActivity.setEditTextPassword(mockPasswordEditText);
        registerActivity.setEditTextConfirmPassword(mockConfirmPasswordEditText);
        registerActivity.setButtonRegister(mockRegisterButton);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void emailValidator_CorrectEmail_ReturnsTrue() {
        assertTrue(registerActivity.isEmailValid("test@example.com"));
    }

    @Test
    public void emailValidator_IncorrectEmail_ReturnsFalse() {
        assertFalse(registerActivity.isEmailValid("test.example.com"));
    }

    @Test
    public void passwordValidator_ValidPassword_ReturnsTrue() {
        assertTrue(registerActivity.isPasswordValid("123456"));
    }

    @Test
    public void passwordValidator_InvalidPassword_ReturnsFalse() {
        assertFalse(registerActivity.isPasswordValid("12345"));
    }

    @Test
    public void passwordsMatch_MatchingPasswords_ReturnsTrue() {
        assertTrue(registerActivity.doPasswordsMatch("123456", "123456"));
    }

    @Test
    public void passwordsMatch_NonMatchingPasswords_ReturnsFalse() {
        assertFalse(registerActivity.doPasswordsMatch("123456", "abcdef"));
    }


}
