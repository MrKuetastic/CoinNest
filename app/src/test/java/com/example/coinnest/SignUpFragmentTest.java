package com.example.coinnest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;



public class SignUpFragmentTest {

    private SignUpFragment signUpFragment;

    @Before
    public void setUp() {
        signUpFragment = new SignUpFragment();

    }

    @Test
    public void isValidEmail_ValidEmail_ReturnsTrue() {
        assertTrue(signUpFragment.isValidEmail("test@example.com"));
    }

    @Test
    public void isValidEmail_InvalidEmail_ReturnsFalse() {
        assertFalse(signUpFragment.isValidEmail("testexample.com"));
        assertFalse(signUpFragment.isValidEmail("test@example"));
        assertFalse(signUpFragment.isValidEmail("@example.com"));
        assertFalse(signUpFragment.isValidEmail("test@.com"));
    }

    @Test
    public void isValidPassword_ValidPassword_ReturnsTrue() {
        assertTrue(signUpFragment.isValidPassword("Valid12!"));
    }

    @Test
    public void isValidPassword_TooShort_ReturnsFalse() {
        assertFalse(signUpFragment.isValidPassword("Sho1!"));
    }

    @Test
    public void isValidPassword_MissingLowercase_ReturnsFalse() {
        assertFalse(signUpFragment.isValidPassword("INVALID1!"));
    }

    @Test
    public void isValidPassword_MissingUppercase_ReturnsFalse() {
        assertFalse(signUpFragment.isValidPassword("invalid1!"));
    }

    @Test
    public void isValidPassword_MissingDigit_ReturnsFalse() {
        assertFalse(signUpFragment.isValidPassword("Invalid!"));
    }

    @Test
    public void isValidPassword_MissingSpecialCharacter_ReturnsFalse() {
        assertFalse(signUpFragment.isValidPassword("Invalid1"));
    }

    @Test
    public void isValidDOB_ValidDOB_ReturnsTrue() {
        assertTrue(signUpFragment.isValidDOB("2001-12-15"));
    }

    @Test
    public void isValidDOB_InvalidDOB_ReturnsFalse() {
        assertFalse(signUpFragment.isValidDOB("12/15/2001")); // Using '/' instead of '-'
        assertFalse(signUpFragment.isValidDOB("2001-15-12")); // Day and month swapped
        assertFalse(signUpFragment.isValidDOB("01-12-2001")); // Year in two digits
        assertFalse(signUpFragment.isValidDOB("2001-00-15")); // Invalid month (00)
        assertFalse(signUpFragment.isValidDOB("2001-12-32")); // Invalid day (32)
    }

    @Test
    public void isValidPhoneNumber_ValidPhoneNumber_ReturnsTrue() {
        assertTrue(signUpFragment.isValidPhoneNumber("+1234567890"));
        assertTrue(signUpFragment.isValidPhoneNumber("1234567890"));
    }

    @Test
    public void isValidPhoneNumber_InvalidPhoneNumber_ReturnsFalse() {
        assertFalse(signUpFragment.isValidPhoneNumber("123")); // Too short
        assertFalse(signUpFragment.isValidPhoneNumber("+123")); // Too short
        assertFalse(signUpFragment.isValidPhoneNumber("12345678901234")); // Too long
        assertFalse(signUpFragment.isValidPhoneNumber("abcdefg")); // Contains non-numeric characters
    }


    @Test
    public void testGetEmail() throws NoSuchFieldException, IllegalAccessException {
        // Given
        String testEmail = "test@example.com";
        setPrivateField(signUpFragment, "email", testEmail);

        // When
        String email = signUpFragment.getEmail();

        // Then
        assertEquals("Getter for email did not return the correct value.", testEmail, email);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }


}
