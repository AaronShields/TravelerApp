package com.example.my_traveler_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event extends AppCompatActivity {
    private Context context;
    private LinearLayout cardViewContainer;

    private static final Map<String, EventData> uscEvents = new HashMap<>();
    private static final Map<String, EventData> laEvents = new HashMap<>();


    // List to store selected event names
    private final List<String> selectedEventNames = new ArrayList<>(); // Add this line

    // Map to store selected event details
    private final Map<String, EventData> selectedEvents = new HashMap<>();

    // whether the event is selected or not
    private final Map<String, Boolean> checkboxStates = new HashMap<>();

    private EditText daysView;
    private TextView textViewLA, textViewUSC;
    private Button finalizePlanButton;
    private Button finalizetripbtn;
    private View uscSubheading;
    private View laSubheading;

    private PopupWindow popupWindow;


    private String currentTitle; // Add this variable

    private Drawable mWindowBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        FirebaseApp.initializeApp(this);

        // Get a reference to the "events" node in your Firebase Realtime Database
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        Log.d("EventsRef", "eventsRef content: " + eventsRef);

        textViewLA = findViewById(R.id.textViewLA);
        textViewUSC = findViewById(R.id.textViewUSC);

        // Initialize the LA title to be underlined since that is what is shown by default
        textViewLA.setTextColor(getResources().getColor(R.color.colorSelected));
        Log.d("textViewLA Paint Flags", "textViewLA.getPaintFlags(): " + textViewLA.getPaintFlags());
        textViewLA.setPaintFlags(textViewLA.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        context = this;

        // Initialize the cardViewContainer
        cardViewContainer = findViewById(R.id.cardViewContainer);
       finalizetripbtn = findViewById(R.id.finalizetrip);

        finalizetripbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the Day1 activity
                Intent intent = new Intent(Event.this, Day1.class);
                EditText daysView = findViewById(R.id.days_box);
                int numSelectedEvents = selectedEventNames.size();
                if (daysView != null) {
                    String daysValue = daysView.getText().toString().trim();
                    if (!daysValue.isEmpty()) {
                        intent.putExtra("selectedDays", daysValue);
                        intent.putExtra("numofevents", numSelectedEvents);
                        intent.putStringArrayListExtra("selectedEventNames", new ArrayList<>(selectedEventNames));

                        // Store addresses of selected events in a list
                        ArrayList<String> selectedEventAddresses = new ArrayList<>();
                        for (String eventName : selectedEventNames) {
                            EventData eventData = selectedEvents.get(eventName);
                            if (eventData != null) {
                                selectedEventAddresses.add(eventData.address);
                            }
                        }
                        intent.putStringArrayListExtra("selectedEventAddresses", selectedEventAddresses);

                        Log.d("DaysValue", "Days Value received: " + daysValue);
                        Log.d("numSelectedEvents", "number of events: " + numSelectedEvents);
                        Log.d("SelectedEventAddresses","Address tracker: " + selectedEventAddresses);

                        // Start the Day1 activity using the created intent
                        startActivity(intent);
                    } else {
                        Log.d("DaysValue", "Value is empty or null");
                    }
                } else {
                    Log.d("DaysView", "DaysView is null");
                }
            }
        });




        // Read data from the "events" node
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String eventName = eventSnapshot.getKey();
                    Log.d("eventSnapshot", "eventSnapshot content: " + eventSnapshot);
                    Log.d("eventName", "eventName: " + eventName);

                    // Check if the event belongs to USC or Los Angeles
                    if (eventName.equals("USC")) {
                        // This event belongs to USC
                        Log.d("debug", "Made it into equals USC");
                        Log.d("eventSnapshot Child", "eventSnapshot Child USC: " + eventSnapshot.child("USC"));
                        populateMap(eventSnapshot, uscEvents);
                    } else if (eventName.equals("LosAngeles")) {
                        // This event belongs to Los Angeles
                        Log.d("debug", "Made it into equals LA");
                        populateMap(eventSnapshot, laEvents);
                    }
                }
                // Now, uscEvents and laEvents maps should be populated with data

                // How is uscEvents and laEvents getting populated?
                Log.d("Event - Main Activity", "uscEvents: " + uscEvents);
                Log.d("Event - Main Activity", "laEvents: " + laEvents);

                // Initially show LA Event cards upon rendering
                createEventCards(laEvents, "laEvents");  // For Los Angeles events
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors
                Log.e("YourActivity", "Error: " + error.getMessage());
            }
        });
    }

    /*
    private void showPopup() {
        // Inflate the layout for the pop-up window
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        // Set up the NumberPicker and Button inside the pop-up
        NumberPicker numberPicker = popupView.findViewById(R.id.numberPicker);

        // maximum of 3 locations per day and minimum of 1 per day
        int minTourLength = selectedEvents.size()/3;
        int maxTourLength = selectedEvents.size();

        // Set NumberPicker tour length range
        numberPicker.setMinValue(minTourLength);
        numberPicker.setMaxValue(maxTourLength);

        // Create a PopupWindow with the inflated layout
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);


        finalizePlanButton = findViewById(R.id.buttonFinalizePlan);


        finalizePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the RoutePlanning activity
                Intent intent = new Intent(Event.this, RoutePlanning.class);

                int selectedTourLength = numberPicker.getValue();

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

                popupWindow.dismiss();

                startActivity(intent);
            }
        });

        // Show the pop-up window at a specific location on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    */



    /**
     * Creates event cards for a given set of events and adds them to the cardViewContainer.
     *
     * @param events A map of events where the key is the event name and the value is the EventData.
     */
    private void createEventCards(Map<String, EventData> events, String eventsName) {

        // Clear the existing cards to only show the selected location's events
        cardViewContainer.removeAllViews();


        // Add subheadings if using the scrolling view for the locations
//        if (eventsName.equals("laEvents")) {
//            addSubheading("Los Angeles");
//        }
//
//        if (eventsName.equals("uscEvents")) {
//            addSubheading("University of Southern California");
//        }

        for (String eventName : events.keySet()) {
            EventData eventData = events.get(eventName);

            Log.d("createEventCards", "eventName: " + eventName + ", eventData: " + eventData);
            createEventCardView(eventName, eventData, eventName);
            Log.d("EventCreation", "Created card for event: " + eventName);
        }
        Log.d("EventCreation", "Number of events: " + events.size());
    }


    private void addSubheading(String subheadingText) {
        // Inflate the Subheading layout from XML
        View subheadingLayout = LayoutInflater.from(context).inflate(R.layout.subheading_template, null);

        // Find the TextView for subheading
        TextView subheadingTextView = subheadingLayout.findViewById(R.id.subheadingTextView);

        // Set the subheading text
        subheadingTextView.setText(subheadingText);

        // Add the subheading to the cardViewContainer
        cardViewContainer.addView(subheadingLayout);
    }


    private void populateMap(DataSnapshot locationSnapshot, Map<String, EventData> eventsMap) {
        for (DataSnapshot location : locationSnapshot.getChildren()) {
            String locationKey = location.getKey();
            Log.d("populateMap", "locationKey: " + locationKey);
            Log.d("populateMap", "location: " + location);
            // Retrieve data from the database
            String address = location.child("address").getValue(String.class);
            Log.d("populateMap", "address: " + location.child("address").getValue(String.class));
            String fee = location.child("fee").getValue(String.class);
            Log.d("populateMap", "fee: " + location.child("fee").getValue(String.class));
            Long openingTime = location.child("openingTime").getValue(Long.class);
            Log.d("populateMap", "openingTime: " + location.child("openingTime").getValue(Long.class));
            Long closingTime = location.child("closingTime").getValue(Long.class);
            Log.d("populateMap", "closingTime: " + location.child("closingTime").getValue(Long.class));
            String travelTime = location.child("travelTime").getValue(String.class);
            Log.d("populateMap", "travelTime: " + location.child("travelTime").getValue(String.class));
            String description = location.child("description").getValue(String.class);
            Log.d("populateMap", "description: " + location.child("description").getValue(String.class));

            // Create EventData object and add it to the events map
            EventData eventData = new EventData(address, fee, openingTime, closingTime, travelTime, description);
            eventsMap.put(locationKey, eventData);
        }
        Log.d("populateMap", "eventsMap: " + eventsMap);
    }



    private void createEventCardView(String eventName, EventData eventData, final String eventNameKey) {
        // Inflate the CardView layout from XML with a margin
        View cardViewLayout = LayoutInflater.from(context).inflate(R.layout.cardview_template, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.card_margin_bottom));
        cardViewLayout.setLayoutParams(layoutParams);

        // Find TextViews for event name and details
        TextView nameTextView = cardViewLayout.findViewById(R.id.locationNameTextView);
        TextView addressTextView = cardViewLayout.findViewById(R.id.locationAddressTextView);
        TextView descriptionTextView = cardViewLayout.findViewById(R.id.locationDescriptionTextView);

        // Find the CheckBox for this event
        CheckBox eventCheckBox = cardViewLayout.findViewById(R.id.checkBox);

        // Ensures the checkbox is checked when switching back and forth between tabs
        if (selectedEvents.containsKey(eventName)) {
            eventCheckBox.setChecked(true);
            checkboxStates.put(eventNameKey, true); // Store checkbox state
        }

        // Set the event name and details
        nameTextView.setText(eventName);
        addressTextView.setText(eventData.address);
        descriptionTextView.setText(eventData.description);

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


    public void onFilterClick(View view) {
        // Reset text colors and underlines
        resetLocationStyles();

        // Highlight the selected text and add underline
        TextView textView = (TextView) view;
        
        textView.setTextColor(getResources().getColor(R.color.colorSelected));
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Update your event list based on the selected location
        // Add your filtering logic here
        String locationName = textView.getText().toString();
        
        if (locationName.equals("USC")) {
            createEventCards(uscEvents, "uscEvents");
            currentTitle = "USC";
        } else if (locationName.equals("LA")) {
            createEventCards(laEvents, "laEvents");
            currentTitle = "LA";
        }

        // Update checkbox states based on the currentTitle
        updateCheckboxStates();
    }

    private void updateCheckboxStates() {
        for (String eventName : selectedEventNames) {
            CheckBox checkBox = findCheckBoxByEventName(eventName);
            if (checkBox != null) {
                checkBox.setChecked(true);
            }
        }
    }

    private CheckBox findCheckBoxByEventName(String eventName) {
        // Find the CheckBox by event name in the cardViewContainer
        for (int i = 0; i < cardViewContainer.getChildCount(); i++) {
            View child = cardViewContainer.getChildAt(i);
            if (child instanceof LinearLayout) {
                CheckBox checkBox = child.findViewById(R.id.checkBox);
                String eventKey = checkBox.getTag().toString();
                if (eventKey.equals(eventName)) {
                    return checkBox;
                }
            }
        }
        return null;
    }


    private void resetLocationStyles() {
        // Reset LA
        TextView textViewLA = findViewById(R.id.textViewLA);
        textViewLA.setTextColor(getResources().getColor(R.color.colorUnselected));
        textViewLA.setPaintFlags(textViewLA.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);

        // Reset USC
        TextView textViewUSC = findViewById(R.id.textViewUSC);
        textViewUSC.setTextColor(getResources().getColor(R.color.colorUnselected));
        textViewUSC.setPaintFlags(textViewUSC.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
    }



    public static class EventData implements Parcelable {
        String address;
        String fee;
        Long openingTime;
        Long closingTime;

        String travelTime;
        String description;

        public EventData() {
            // Default constructor required for Firebase
        }

        public EventData(String address, String fee, Long openingTime, Long closingTime, String travelTime, String description) {
            this.address = address;
            this.fee = fee;
            this.openingTime = openingTime;
            this.closingTime = closingTime;
            this.travelTime = travelTime;
            this.description = description;
        }

        protected EventData(Parcel in) {
            address = in.readString();
            fee = in.readString();
            openingTime = in.readLong();
            closingTime = in.readLong();
            travelTime = in.readString();
            description = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(address);
            dest.writeString(fee);
            dest.writeLong(openingTime);
            dest.writeLong(closingTime);
            dest.writeString(travelTime);
            dest.writeString(description);
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

    /*
    private static final Map<String, EventData> events = new HashMap<String, EventData>() {{
        /* put("Brittingham Intramural Field", new EventData("3506 McClintock Ave, Los Angeles, CA 90089", "Free for students", "24/7 for students", "10 min"));
        /*put("Doheny Library", new EventData("3550 Trousdale Pkwy, Los Angeles, CA 90089", "Free", "9am - 10pm", "11 min"));
        put("Fertitta Hall", new EventData("Registration Building, 610 Childs Way, Los Angeles, CA 90089", "Free", "7am - 8pm", "8 min"));
        put("Fisher Museum of Art", new EventData("823 W Exposition Blvd, Los Angeles, CA 90089", "Free", "12pm - 5pm", "9 min"));
        put("Lyon Center", new EventData("1026 W 34th St, Los Angeles, CA 90089", "Free for students", "6am - 11pm", "15 min"));
        put("McCarthy Quad", new EventData("3551 Trousdale Pkwy, Los Angeles, CA 90089", "Free", "24/7 for students", "12 min"));
        put("Ronald Tutor Center", new EventData("3607 Trousdale Pkwy, Los Angeles, CA 90089", "Free", "7am - 12am", "14 min"));
        put("Tommy Trojan", new EventData("34.02051°N 118.28563°W", "Free", "24/7 for students", "13 min"));
        put("Village Hecuba Statue", new EventData("3201 Hoover St, Los Angeles, CA 90007", "Free", "24/7 for students", "10 min"));
    }}; */
}
