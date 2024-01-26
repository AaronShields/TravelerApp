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

public class LaEvent extends AppCompatActivity {
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
        for (String eventName : LaEvent.events.keySet()) {
            LaEvent.EventData eventData = LaEvent.events.get(eventName);
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
                Intent intent = new Intent(LaEvent.this, RoutePlanning.class);

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

        Button multidayButton = findViewById(R.id.multi_day);
        multidayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the MultidayPlanning activity
                Intent intent = new Intent(LaEvent.this, MultidayPlanningUSC.class);

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

    public static final Map<String, EventData> events = new HashMap<String, EventData>() {{
        put("Angel’s Flight", new EventData("350 S Grand Ave, Los Angeles, CA 90071", "$1", "7am - 10pm", "15 min"));
        put("Chinese Theater", new EventData("6925 Hollywood Blvd, Hollywood, CA 90028", "$38", "7am -10pm", "27 min"));
        put("Getty Museum", new EventData("1200 Getty Center Dr, Los Angeles, CA 90049", "$89", "10am - 8pm", "30 min"));
        put("Grand Central Market", new EventData("317 S Broadway, Los Angeles, CA 90013", "Free", "8am - 9pm", "12 min"));
        put("Griffith Park Observatory", new EventData("2800 E Observatory Rd, Los Angeles, CA 90027", "Free", "12pm - 10pm", "38 min"));
        put("Hollywood Sign", new EventData("Hollywood Sign, Los Angeles, CA 90068", "Free", "12pm -11pm", "12 min"));
        put("Hollywood Walk of Fame", new EventData("Hollywood Walk of Fame, 6901 Hollywood Blvd, Los Angeles, CA 90028", "Free", "24/7", "27 min"));
        put("La Brea Tar Pits and Museum", new EventData("La Brea Tar Pits Museum Parking, Unnamed Road, Los Angeles, CA 90036", "$144", "9am - 5pm", "24 min"));
        put("Santa Monica Pier", new EventData("Santa Monica Pier, 200 Santa Monica Pier, Santa Monica, CA 90401", "Free", "12pm-10pm", "33 min"));
        put("The Broad", new EventData("221 S Grand Ave, Los Angeles, CA 90012", "Free", "11am - 5pm", "13 min"));
        put("Universal Studios Hollywood", new EventData("100 Universal City Plaza, Universal City, CA 91608", "$109", "12pm-10pm", "8 min"));
        put("Venice Beach", new EventData("1800 Ocean Front Walk, Venice, CA 90291", "Free", "12pm-11pm", "10 min"));
    }};
}
