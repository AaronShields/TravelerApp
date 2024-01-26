package com.example.my_traveler_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class location_selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        // Parse Location information
        JSONParser.parseJSON(this);

        ImageButton uscButton = findViewById(R.id.uscButton);
        ImageButton laButton = findViewById(R.id.laButton);

        uscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the UscEventActivity
                Intent intent = new Intent(location_selection.this, UscEvent.class);

                // Start the UscEvent activity
                startActivity(intent);
            }
        });

        laButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the LaEventActivity
                Intent intent = new Intent(location_selection.this, LaEvent.class);

                // Start the LaEvent activity
                startActivity(intent);
            }
        });
    }
}
