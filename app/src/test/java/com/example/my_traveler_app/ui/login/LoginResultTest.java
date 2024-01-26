package com.example.my_traveler_app.ui.login;

import junit.framework.TestCase;
import org.junit.Test;

public class LoginResultTest extends TestCase {

    @Test
    public void testConstructorWithSuccess() {
        LoggedInUserView loggedInUser = new LoggedInUserView("user123");
        LoginResult loginResult = new LoginResult(loggedInUser);

        assertEquals(loggedInUser, loginResult.getSuccess());
        assertNull(loginResult.getError());
    }

    @Test
    public void testConstructorWithError() {
        String errorMessage = "Invalid credentials";
        LoginResult loginResult = new LoginResult(errorMessage);

        assertEquals(errorMessage, loginResult.getError());
        assertNull(loginResult.getSuccess());
    }

    @Test
    public void testGetters() {
        LoggedInUserView loggedInUser = new LoggedInUserView("user123");
        LoginResult loginResult = new LoginResult(loggedInUser);

        assertEquals(loggedInUser, loginResult.getSuccess());
        assertNull(loginResult.getError());

        String errorMessage = "Invalid credentials";
        loginResult = new LoginResult(errorMessage);

        assertEquals(errorMessage, loginResult.getError());
        assertNull(loginResult.getSuccess());
    }
}
