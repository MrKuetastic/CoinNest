package com.example.coinnest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

public class LoginFragmentTest {

    private LoginFragment loginFragment;
    private final int testUserId = 123; // Corrected to int to match the userId type

    @Before
    public void setUp() {
        loginFragment = new LoginFragment();
        try {
            setPrivateField(loginFragment, "userId", testUserId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            // Fail the test if there's an issue setting up the reflection-based field modification
            throw new RuntimeException("Failed to set up test: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserId() {
        // When
        int id = loginFragment.getUserId();

        // Then
        assertEquals("Getter for userId did not return the correct value.", testUserId, id);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
