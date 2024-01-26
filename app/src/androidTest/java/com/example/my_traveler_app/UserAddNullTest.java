package com.example.my_traveler_app;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UserAddNullTest {

    @Rule
    public ActivityScenarioRule<CreateAccount> activityRule =
            new ActivityScenarioRule<>(CreateAccount.class);

    @Test
    public void signUpNewUser() {
        String email = "" + UUID.randomUUID().toString() + "";
        String password = "Test_2";
        String securityQuestionAnswer = "Test2";
        String username = "Test2username";

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
