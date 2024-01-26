package com.example.my_traveler_app;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UserAddIncorrectTest {

    @Rule
    public ActivityScenarioRule<CreateAccount> activityRule =
            new ActivityScenarioRule<>(CreateAccount.class);

    @Test
    public void signUpNewUser() {
        String email = "user" + UUID.randomUUID().toString() + "@example.com";
        String password = "";
        String securityQuestionAnswer = "Test1";
        String username = "Test1username";
        //no password

        // Enter text in EditText fields
        Espresso.onView(withId(R.id.email_box)).perform(ViewActions.typeText(email));
        Espresso.onView(withId(R.id.password_box)).perform(ViewActions.typeText(password));
        Espresso.onView(withId(R.id.securityquestion_box)).perform(ViewActions.typeText(securityQuestionAnswer));
        Espresso.onView(withId(R.id.usernamebox)).perform(ViewActions.typeText(username));

        // Close the soft keyboard
        Espresso.closeSoftKeyboard();

        // Click the Sign Up button
        Espresso.onView(withId(R.id.signupButton)).perform(ViewActions.click());


    }
}

