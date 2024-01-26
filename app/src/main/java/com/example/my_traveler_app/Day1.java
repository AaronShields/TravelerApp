package com.example.my_traveler_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Day1 extends AppCompatActivity {
    private String address1 = "";
    private String address2 = "";
    private String address3 = "";
    private ArrayList<String> allAddresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day1);

        Button exportMapsButton = findViewById(R.id.export_maps);

        Button mapinAppButton = findViewById(R.id.maps_in_app);
        exportMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if any of the addresses are null
                if (address1 != null && address2 != null && address3 != null) {
                    // Construct the URL for Google Maps directions
                    String url = "https://www.google.com/maps/dir/?api=1";
                    url += "&origin=" + address1.replace(" ", "+");
                    url += "&destination=" + address2.replace(" ", "+");
                    url += "&waypoints=" + address3.replace(" ", "+");

                    // Add start time (current time in milliseconds) to the URL
                    long currentTimeMillis = System.currentTimeMillis();
                    url += "&time=" + currentTimeMillis;

                    // Open the URL in a web browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    // Show a message indicating that all addresses are not available
                    Log.e("Export Maps", "Addresses are not available");
                }
            }
        });

        allAddresses.add(address1);
        allAddresses.add(address2);
        allAddresses.add(address3);

        Button mapsInAppButton = findViewById(R.id.maps_in_app);
        mapsInAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the MapActivity
                Intent mapIntent = new Intent(Day1.this, MapsActivity.class);
                mapIntent.putStringArrayListExtra("allAddresses", allAddresses);
                startActivity(mapIntent);
            }
        });

        // Get the selected event names from the intent
        Intent intent = getIntent();
        String daysValue = intent.getStringExtra("selectedDays");
        Log.d("Daysvalue", "Value received days_activity" + daysValue);
        ArrayList<String> selectedEventNames = intent.getStringArrayListExtra("selectedEventNames");
        ArrayList<String> selectedEventAddresses = intent.getStringArrayListExtra("selectedEventAddresses");
        int numberSelectedEvents = intent.getIntExtra("numofevents", 0);

        TextView daysDisplay = findViewById(R.id.daysDisplay);

        Log.d("selectedDays", "Selected days list" + selectedEventNames);

        // Concatenate "day" with the received value and set it to the TextView
        String displayText = "Day " + daysValue + " Displayed";
        daysDisplay.setText(displayText);

        int selectedDay = Integer.parseInt(daysValue);

// Calculate the starting index based on the selected day
        int startingIndex = (selectedDay - 1) * 2;

// Ensure that the starting index is within the bounds of the list
        if (startingIndex < 0) {
            startingIndex = 0;
        }

        int addressIndex = 0; // Track the address index
        while (addressIndex < 3 && startingIndex < selectedEventAddresses.size()) {
            String eventAddress = selectedEventAddresses.get(startingIndex);

            if (addressIndex == 0) {
                address1 = eventAddress;
                Log.d("Address1", "Address 1: " + address1);
            } else if (addressIndex == 1) {
                address2 = eventAddress;
                Log.d("Address2", "Address 2: " + address2);
            } else if (addressIndex == 2) {
                address3 = eventAddress;
                Log.d("Address3", "Address 3: " + address3);
            }

            startingIndex++;
            addressIndex++;
        }

        int newstartingIndex = (selectedDay - 1) * 2;

// Ensure that the starting index is within the bounds of the list
        if (newstartingIndex < 0) {
            newstartingIndex = 0;
        }


        // Find the CardViews
        CardView cardView1 = findViewById(R.id.cardview1);
        CardView cardView2 = findViewById(R.id.cardview2);
        CardView cardView3 = findViewById(R.id.cardview3);


// Display events in CardViews based on the calculated starting index
        if("20".equals(daysValue)){
            TextView textView1 = cardView1.findViewById(R.id.textview1);
            textView1.setText(selectedEventNames.get(17));
        }
        else if("19".equals(daysValue)){

            TextView textView1 = cardView1.findViewById(R.id.textview1);
            textView1.setText(selectedEventNames.get(16));
        }
        else if("18".equals(daysValue)){
            TextView textView1 = cardView1.findViewById(R.id.textview1);
            textView1.setText(selectedEventNames.get(15));
        }
        else {
            for (int i = 0; i < 3; i++) {
                if (newstartingIndex < selectedEventNames.size()) {
                    String eventName = selectedEventNames.get(newstartingIndex);
                    TextView textView = null;

                    // Set event names in the respective CardViews
                    if (i == 0) {
                        textView = cardView1.findViewById(R.id.textview1);
                    } else if (i == 1) {
                        textView = cardView2.findViewById(R.id.textview2);
                    } else if (i == 2) {
                        textView = cardView3.findViewById(R.id.textview3);
                    }

                    if (textView != null) {
                        textView.setText(eventName);
                    }

                    newstartingIndex++;
                }
            }
        }

    }

}


