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

public class PasswordNullTest {

    @Rule
    public ActivityScenarioRule<ForgotPasswordActivity> activityRule =
            new ActivityScenarioRule<>(ForgotPasswordActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testPasswordReset() {
        // NullTest
        String testEmail = "";
        String testNewPassword = "";
        String testSecurityAnswer = "";

        // Enter email, new password, and security question answer
        Espresso.onView(ViewMatchers.withId(R.id.email_box))
                .perform(ViewActions.typeText(testEmail), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.password_box))
                .perform(ViewActions.typeText(testNewPassword), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.securityquestion_box))
                .perform(ViewActions.typeText(testSecurityAnswer), ViewActions.closeSoftKeyboard());

        // Click on the change password button
        Espresso.onView(ViewMatchers.withId(R.id.changePasswordButton))
                .perform(ViewActions.click());

        //Check to see if landing page is launched to check if button works
        Intents.intended(hasComponent(LandingPage.class.getName()), Intents.times(0));
    }
}
