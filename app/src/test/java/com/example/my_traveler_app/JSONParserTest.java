package com.example.my_traveler_app;

import android.content.ContextWrapper;
import android.content.res.Resources;

import junit.framework.TestCase;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class JSONParserTest extends TestCase {

    public void testParseJSON() {
        // Create a test context
        TestContext testContext = new TestContext();

        // Mock JSON data for testing
        String testData = "{\"events\":{\"location1\":{\"event1\":\"Event One\",\"event2\":\"Event Two\"},\"location2\":{\"event3\":\"Event Three\"}}}";

        // Create an InputStream from the test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        // Set the input stream in the test context
        testContext.setInputStream(inputStream);

        // Call the parseJSON method
        JSONParser.parseJSON(testContext);

        // Check if eventJson is not null after parsing
        assertNotNull(JSONParser.getEventJsonForTesting());

        // Check if the parsed data is as expected
        try {
            JSONObject events = JSONParser.getEventJsonForTesting().getJSONObject("events");
            JSONObject location1 = events.getJSONObject("location1");
            assertEquals("Event One", location1.getString("event1"));
            assertEquals("Event Two", location1.getString("event2"));

            JSONObject location2 = events.getJSONObject("location2");
            assertEquals("Event Three", location2.getString("event3"));
        } catch (Exception e) {
            fail("Exception thrown while testing parseJSON: " + e.getMessage());
        }
    }

    public void testGetEventDataForLocation() {
        // Create a test context
        TestContext testContext = new TestContext();

        // Mock JSON data for testing
        String testData = "{\"events\":{\"location1\":{\"event1\":\"Event One\",\"event2\":\"Event Two\"}}}";

        // Create an InputStream from the test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        // Set the input stream in the test context
        testContext.setInputStream(inputStream);

        // Call the parseJSON method
        JSONParser.parseJSON(testContext);

        // Test getEventDataForLocation method
        JSONObject eventData = JSONParser.getEventDataForLocation("location1", "Event One");
        assertNotNull(eventData);

        try {
            // Check if the retrieved data is as expected
            assertEquals("Event One", eventData.getString("event1"));
            assertEquals("Event Two", eventData.getString("event2"));
        } catch (Exception e) {
            fail("Exception thrown while testing getEventDataForLocation: " + e.getMessage());
        }

        // Test with a non-existing location
        JSONObject nonExistingEventData = JSONParser.getEventDataForLocation("nonexistent", "Event");
        assertNull(nonExistingEventData);
    }

    // TestContext class to provide a mock context with a specific InputStream
    private static class TestContext extends ContextWrapper {
        private InputStream inputStream;

        public TestContext() {
            super(null);
        }

        void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public Resources getResources() {
            return super.getResources(); // Use the base getResources() method
        }

//        @Override
//        public InputStream openRawResource(int id) {
//            return inputStream;
//        }
    }

}
