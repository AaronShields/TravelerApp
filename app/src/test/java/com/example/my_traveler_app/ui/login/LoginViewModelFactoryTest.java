package com.example.my_traveler_app.ui.login;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_traveler_app.ui.login.LoginViewModel;
import com.example.my_traveler_app.ui.login.LoginViewModelFactory;

import junit.framework.TestCase;

import org.junit.Test;

public class LoginViewModelFactoryTest extends TestCase {

    @Test
    public void testCreateUnknownViewModel() {
        LoginViewModelFactory factory = new LoginViewModelFactory();

        // Attempt to create a ViewModel of an unknown type
        try {
            factory.create(AnotherViewModel.class);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Unknown ViewModel class", e.getMessage());
        }
    }

    // Sample class representing another ViewModel (replace with your actual ViewModel)
    private static class AnotherViewModel extends ViewModel {
    }
}
