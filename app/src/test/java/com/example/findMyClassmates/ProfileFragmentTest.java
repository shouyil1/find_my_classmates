package com.example.findMyClassmates;

import static org.junit.Assert.*;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ProfileFragmentTest {

    @Mock
    private FirebaseFirestore mockDB;
    private ProfileFragment profileFragment;

    @Before
    public void setUp() {
        // Initialize ProfileFragment
        MockitoAnnotations.openMocks(this);
        profileFragment = new ProfileFragment("test@example.com");
        profileFragment.setDB(mockDB);
    }

    @Test
    public void whenNameIsValid_thenIsNameValidReturnsTrue() {
        assertTrue(profileFragment.isNameValid("John Doe"));
    }

    @Test
    public void whenNameIsInvalid_thenIsNameValidReturnsFalse() {
        assertFalse(profileFragment.isNameValid(""));
        assertFalse(profileFragment.isNameValid("   "));
    }

    @Test
    public void whenIdIsValid_thenIsIdValidReturnsTrue() {
        assertTrue(profileFragment.isIdValid("123456"));
    }

    @Test
    public void whenIdIsInvalid_thenIsIdValidReturnsFalse() {
        assertFalse(profileFragment.isIdValid(""));
        assertFalse(profileFragment.isIdValid("   "));
    }
}