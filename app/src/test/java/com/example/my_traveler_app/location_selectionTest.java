package com.example.my_traveler_app;

import android.content.Intent;
import android.widget.ImageButton;

import junit.framework.TestCase;

public class location_selectionTest extends TestCase {

    public void testUscButton() {
        location_selection activity = new location_selection();
        activity.onCreate(null);

        ImageButton uscButton = activity.findViewById(R.id.uscButton);
        assertNotNull(uscButton);

        uscButton.performClick();
        Intent startedIntent = getStartedActivityIntent();
        assertNotNull(startedIntent);
        assertEquals(UscEvent.class.getName(), startedIntent.getComponent().getClassName());
    }

    public void testLaButton() {
        location_selection activity = new location_selection();
        activity.onCreate(null);

        ImageButton laButton = activity.findViewById(R.id.laButton);
        assertNotNull(laButton);

        laButton.performClick();
        Intent startedIntent = getStartedActivityIntent();
        assertNotNull(startedIntent);
        assertEquals(LaEvent.class.getName(), startedIntent.getComponent().getClassName());
    }

    // Mock method to simulate starting an activity
    private Intent getStartedActivityIntent() {
        // For simplicity, we are just returning a new Intent here
        return new Intent();
    }
}
