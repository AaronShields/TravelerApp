package com.example.my_traveler_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import junit.framework.TestCase;

import java.io.Serializable;


public class LaEventTest extends TestCase {

    private LaEvent activity;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = new LaEvent();
    }

    // Test onCreate method to ensure event card creation
    public void testOnCreate() {
        // Set up dummy data for the intent
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        bundle.putSerializable("eventAddressesList", (Serializable) new LaEvent.EventData("Test Address", "$10", "9am-5pm", "20 min"));
        intent.putExtras(bundle);
        activity.setIntent(intent);

        // Call onCreate method
        activity.onCreate(null);

        // Ensure that the cardViewContainer is initialized
        LinearLayout cardViewContainer = activity.findViewById(R.id.cardViewContainer);
        assertNotNull(cardViewContainer);

        // Ensure that event cards are created based on the dummy data
        int childCount = cardViewContainer.getChildCount();
        assertTrue(childCount > 0);

        // Ensure that the "single_day" button is initialized
        Button singleDayButton = activity.findViewById(R.id.single_day);
        assertNotNull(singleDayButton);

        // Perform a click on the "single_day" button
        singleDayButton.performClick();

        // Ensure that the RoutePlanning activity is started
        Intent startedIntent = getStartedActivityIntent();
        assertNotNull(startedIntent);
        assertEquals(RoutePlanning.class.getName(), startedIntent.getComponent().getClassName());
    }

    // Mock method to simulate starting an activity
    private Intent getStartedActivityIntent() {
        // For simplicity, we are just returning a new Intent here
        return new Intent();
    }
}
