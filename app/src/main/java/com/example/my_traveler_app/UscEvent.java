package com.example.my_traveler_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;

public class UscEvent extends AppCompatActivity {
    private Context context;
    private LinearLayout cardViewContainer;

    // List to store selected event names
    private List<String> selectedEventNames = new ArrayList<>(); // Add this line

    // Map to store selected event details
    private Map<String, EventData> selectedEvents = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_la_event);

        context = this;
        // Initialize the cardViewContainer
        cardViewContainer = findViewById(R.id.cardViewContainer);

        // Your existing code here

        // Iterate over the events and create event cards
        for (String eventName : UscEvent.events.keySet()) {
            UscEvent.EventData eventData = UscEvent.events.get(eventName);
            createEventCardView(eventName, eventData, eventName);
            //Log.d("EventCreation", "Created card for event: " + eventName);
        }

        // Find the "single_day" button by its ID
        Button singleDayButton = findViewById(R.id.single_day);

        // Set an OnClickListener for the "single_day" button
        singleDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the RoutePlanning activity
                Intent intent = new Intent(UscEvent.this, RoutePlanningUSC.class);

                // Create an ArrayList to store Parcelable EventData objects
                ArrayList<EventData> eventDataList = new ArrayList<>();
                for (String eventName : selectedEventNames) {
                    EventData eventData = selectedEvents.get(eventName);
                    eventDataList.add(eventData);
                }

                // Pass the ArrayList of Parcelable objects to the intent
                intent.putParcelableArrayListExtra("selectedEventInfo", eventDataList);

                // Pass the list of selected event names as an extra
                intent.putStringArrayListExtra("selectedEventNames", new ArrayList<>(selectedEventNames));

                startActivity(intent);
            }
        });

        // Find the "single_day" button by its ID
        Button multiDayButton = findViewById(R.id.multi_day);

        // Set an OnClickListener for the "single_day" button
        multiDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the RoutePlanning activity
                Intent intent = new Intent(UscEvent.this, MultidayPlanningUSC.class);

                // Create an ArrayList to store Parcelable EventData objects
                ArrayList<EventData> eventDataList = new ArrayList<>();
                for (String eventName : selectedEventNames) {
                    EventData eventData = selectedEvents.get(eventName);
                    eventDataList.add(eventData);
                }

                // Pass the button name to the intent
                intent.putExtra("buttonType", "multiDayButton");

                // Pass the ArrayList of Parcelable objects to the intent
                intent.putParcelableArrayListExtra("selectedEventInfo", eventDataList);

                // Pass the list of selected event names as an extra
                intent.putStringArrayListExtra("selectedEventNames", new ArrayList<>(selectedEventNames));

                startActivity(intent);
            }
        });
    }


    private void createEventCardView(String eventName, EventData eventData, final String eventNameKey) {
        // Inflate the CardView layout from XML
        View cardViewLayout = LayoutInflater.from(context).inflate(R.layout.cardview_template, null);

        // Find the CardView and set its properties
        CardView cardView = cardViewLayout.findViewById(R.id.cardView);

        // Find TextViews for event name and details
        TextView eventNameTextView = cardViewLayout.findViewById(R.id.eventNameTextView);
        TextView eventDetailsTextView = cardViewLayout.findViewById(R.id.eventDetailsTextView);

        // Find the CheckBox for this event
        CheckBox eventCheckBox = cardViewLayout.findViewById(R.id.checkBox);

        // Set the event name and details
        eventNameTextView.setText(eventName);
        eventDetailsTextView.setText("Address: " + eventData.address + "\n" +
                "Fee: " + eventData.fee + "\n" +
                "Hours: " + eventData.hours + "\n" +
                "Travel Time: " + eventData.travelTime);

        // Add the CardView to the cardViewContainer
        cardViewContainer.addView(cardViewLayout);

        // Set an OnCheckedChangeListener for the CheckBox
        eventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Add or remove the event name from selectedEventNames based on CheckBox state
                if (isChecked) {
                    selectedEventNames.add(eventNameKey);
                    selectedEvents.put(eventNameKey, eventData);
                    Log.d("SelectedEvents", "Selected event: " + eventNameKey); // Log the selected event
                    Log.d("SelectedEvents", "Selected events: " + selectedEvents);
                } else {
                    selectedEventNames.remove(eventNameKey);
                    selectedEvents.remove(eventNameKey);
                }
            }
        });
    }

    public static class EventData implements Parcelable {
        String address;
        String fee;
        String hours;
        String travelTime;

        public EventData(String address, String fee, String hours, String travelTime) {
            this.address = address;
            this.fee = fee;
            this.hours = hours;
            this.travelTime = travelTime;
        }

        protected EventData(Parcel in) {
            address = in.readString();
            fee = in.readString();
            hours = in.readString();
            travelTime = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(address);
            dest.writeString(fee);
            dest.writeString(hours);
            dest.writeString(travelTime);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<EventData> CREATOR = new Creator<EventData>() {
            @Override
            public EventData createFromParcel(Parcel in) {
                return new EventData(in);
            }

            @Override
            public EventData[] newArray(int size) {
                return new EventData[size];
            }
        };
    }

    private static final Map<String, EventData> events = new HashMap<String, EventData>() {{
        put("Brittingham Intramural Field", new EventData("3506 McClintock Ave, Los Angeles, CA 90089", "Free for students", "24/7 for students", "10 min"));
        put("Doheny Library", new EventData("3550 Trousdale Pkwy, Los Angeles, CA 90089", "Free", "9am - 10pm", "11 min"));
        put("Fertitta Hall", new EventData("Registration Building, 610 Childs Way, Los Angeles, CA 90089", "Free", "7am - 8pm", "8 min"));
        put("Fisher Museum of Art", new EventData("823 W Exposition Blvd, Los Angeles, CA 90089", "Free", "12pm - 5pm", "9 min"));
        put("Lyon Center", new EventData("1026 W 34th St, Los Angeles, CA 90089", "Free for students", "6am - 11pm", "15 min"));
        put("McCarthy Quad", new EventData("3551 Trousdale Pkwy, Los Angeles, CA 90089", "Free", "24/7 for students", "12 min"));
        put("Ronald Tutor Center", new EventData("3607 Trousdale Pkwy, Los Angeles, CA 90089", "Free", "7am - 12am", "14 min"));
        put("Tommy Trojan", new EventData("34.02051°N 118.28563°W", "Free", "24/7 for students", "13 min"));
        put("Village Hecuba Statue", new EventData("3201 Hoover St, Los Angeles, CA 90007", "Free", "24/7 for students", "10 min"));
    }};
}
