package com.example.my_traveler_app;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.my_traveler_app.location_selection;
import com.example.my_traveler_app.R;
import com.example.my_traveler_app.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;

public class UserLoginFail {

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
    public void loginWithInvalidCredentials() {
        String invalidUsername = "invalidUser@example.com";
        String invalidPassword = "invalidPassword";

        Espresso.onView(ViewMatchers.withId(R.id.username_box))
                .perform(ViewActions.typeText(invalidUsername));
        Espresso.onView(ViewMatchers.withId(R.id.password_box))
                .perform(ViewActions.typeText(invalidPassword));
        Espresso.closeSoftKeyboard(); // Close the keyboard if it's open

        Espresso.onView(ViewMatchers.withId(R.id.loginButton))
                .perform(ViewActions.click());

        android.util.Log.d("TestLog", "After clicking loginButton");


        Intents.intended(not(hasComponent(location_selection.class.getName())), Intents.times(0));

        android.util.Log.d("TestLog", "Intents.times(1)?: " + Intents.times(0));
    }
}
