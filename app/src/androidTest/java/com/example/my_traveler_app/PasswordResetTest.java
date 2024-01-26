package com.example.my_traveler_app;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

public class PasswordResetTest {

    // Rule to launch the ForgotPasswordActivity
    @Rule
    public ActivityScenarioRule<ForgotPasswordActivity> activityRule =
            new ActivityScenarioRule<>(ForgotPasswordActivity.class);

    @Test
    public void testPasswordReset() {
        // guest@usc.edu fake test
        String testEmail = "guest@usc.edu";
        String testNewPassword = "USC";
        String testSecurityAnswer = "Trojan";

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

    }
}

