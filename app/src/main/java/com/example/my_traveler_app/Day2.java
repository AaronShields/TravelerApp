package com.example.my_traveler_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Day2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day1);

        // Get the selected event names from the intent
        Intent intent = getIntent();
        ArrayList<String> selectedEventNames = intent.getStringArrayListExtra("selectedEventNames");

        // Find the CardViews
        CardView cardView1 = findViewById(R.id.cardview1);
        CardView cardView2 = findViewById(R.id.cardview2);
        CardView cardView3 = findViewById(R.id.cardview3);

        // Display event names in CardViews if they exist
        if (selectedEventNames != null && selectedEventNames.size() >= 3) {
            // Set event names in the respective CardViews
            TextView textView1 = cardView1.findViewById(R.id.textview1);
            textView1.setText(selectedEventNames.get(3));

            TextView textView2 = cardView2.findViewById(R.id.textview2);
            textView2.setText(selectedEventNames.get(4));

            TextView textView3 = cardView3.findViewById(R.id.textview3);
            textView3.setText(selectedEventNames.get(5));
        }
    }}