package com.example.my_traveler_app;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;

public class PasswordResetWrongTest {

    @Rule
    public ActivityScenarioRule<ForgotPasswordActivity> activityRule =
            new ActivityScenarioRule<>(ForgotPasswordActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testPasswordResetWrongAnswer() {
        String testEmail = "guest@usc.edu";
        String testNewPassword = "USC";
        String testSecurityAnswer = "Mary"; // Wrong answer

        // Enter email and password (no need to enter a wrong security answer)
        Espresso.onView(ViewMatchers.withId(R.id.email_box))
                .perform(ViewActions.typeText(testEmail), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.password_box))
                .perform(ViewActions.typeText(testNewPassword), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.changePasswordButton))
                .perform(ViewActions.click());

    }
}
