package com.example.my_traveler_app;

import android.content.Context;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.InputStream;

public class JSONParser {
    private static JSONObject eventJson; // Declare eventJson as a class field

    public static void parseJSON(Context context) {
        try {
            // Open the JSON file from the raw resources
            InputStream inputStream = context.getResources().openRawResource(R.raw.data);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            eventJson = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getEventDataForLocation(String location, String selectedLocation) {
        if (eventJson != null) {
            try {
                return eventJson.getJSONObject("events").getJSONObject(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject getEventJsonForTesting() {
        return eventJson;
    }
}
