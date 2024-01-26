package com.example.my_traveler_app;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.my_traveler_app.LandingPage;
import com.example.my_traveler_app.R;
import com.example.my_traveler_app.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class UserLoginSuccess {

    @Rule
    public ActivityScenarioRule<LandingPage> activityRule =
            new ActivityScenarioRule<>(LandingPage.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void loginWithValidCredentials() {

        String validUsername = "guest@usc.edu";
        String validPassword = "FightOn!";

        // Find the EditText fields and the login button and perform actions
        Espresso.onView(ViewMatchers.withId(R.id.username_box))
                .perform(ViewActions.typeText(validUsername));
        Espresso.onView(ViewMatchers.withId(R.id.password_box))
                .perform(ViewActions.typeText(validPassword));
        Espresso.closeSoftKeyboard(); // Close the keyboard if it's open

        Espresso.onView(ViewMatchers.withId(R.id.loginButton))
                .perform(ViewActions.click());

        Intents.intended(not(hasComponent(LandingPage.class.getName())));
    }
}

