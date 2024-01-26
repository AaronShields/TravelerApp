package com.example.my_traveler_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.my_traveler_app.UscEvent.EventData
import java.util.ArrayList
import java.util.regex.Pattern

class RoutePlanningUSC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_planning)

        // Get the list of selected event names from the intent
        val selectedEventNames = intent.getStringArrayListExtra("selectedEventNames") ?: ArrayList()
        val selectedEventInfo = intent.getParcelableArrayListExtra<EventData>("selectedEventInfo") ?: ArrayList()

        // Find the TextView in your layout by its ID
        val eventListTextView = findViewById<TextView>(R.id.eventListTextView)

        // Initialize the clock time to 9:00 AM
        var clock: Double = 9.0

        // Regular expression pattern to match hours like "7am"
        val pattern = Pattern.compile("(\\d+)am")

        // Create a StringBuilder to build the text
        val eventListText = StringBuilder()
        eventListText.append("One Day Trip\n\n")

        // Create a list to store the addresses
        val eventAddresses = ArrayList<String>()

        // Loop through selectedEventInfo
        for (i in selectedEventNames.indices) {
            val eventName = selectedEventNames[i]
            val eventData = selectedEventInfo[i]

            val hoursString = eventData.hours

            val matcher = pattern.matcher(hoursString)
            if (matcher.find()) {
                val hour = matcher.group(1).toInt()
                val militaryHour = if (hour == 12) 0 else hour

                // Calculate the time for this event
                val startTime = clock
                clock += militaryHour.toDouble() // Explicitly convert militaryHour to Double
                eventListText.append("$eventName\n")

                // Add 30 minutes for expected event time
                clock += 0.5

                // Add travel time to the clock
                val travelTime = eventData.travelTime
                clock += getTravelTimeInHours(travelTime)

                // Calculate the elapsed time
                val endTime = clock
                var elapsedTime = endTime - startTime

                // Check if end time exceeds 21:00 (9:00 PM) and elapsed time is within 1.5 hours
                if (endTime <= 21.0 && elapsedTime <= 1.5) {
                    eventListText.append("Start Time: ${formatTime(startTime)}\n")
                    eventListText.append("Elapsed Time: ${formatTime(elapsedTime)}\n")
                    eventListText.append("End Time: ${formatTime(endTime)}\n\n")

                    // Add the event address to the list
                    eventAddresses.add(eventData.address)

                    // Set the start time for the next event (30 minutes after the current end time)
                    clock = endTime + 0.5
                } else {
                    // If elapsed time exceeds 1.5 hours or end time exceeds 9:00 PM, adjust to 1.5 hours
                    eventListText.append("Start Time: ${formatTime(startTime)}\n")
                    eventListText.append("Elapsed Time: 1.5 hours\n")
                    eventListText.append("End Time: ${formatTime(startTime + 1.5)}\n\n")

                    // Adjust elapsed time to 1.5 hours
                    elapsedTime = 1.5

                    // Set the start time for the next event (30 minutes after the adjusted end time)
                    clock = startTime + 1.5 + 0.5
                }
            }
        }

        // Set the text in the TextView to display the selected events
        eventListTextView.text = eventListText.toString()

        val mapButton = findViewById<Button>(R.id.mapButton)

        // Set an OnClickListener for the "Start Map" button
        mapButton.setOnClickListener {
            // Create an Intent to start the SingleDayMapsActivity
            val intent = Intent(this, SingleDayMapsActivity::class.java)
            // Pass the list of event addresses to SingleDayMapsActivity
            intent.putStringArrayListExtra("eventAddresses", eventAddresses)
            startActivity(intent)
        }
    }

    private fun formatTime(hour: Double): String {
        val hourPart = hour.toInt()
        val minutePart = ((hour - hourPart) * 60).toInt()
        return String.format("%02d:%02d", hourPart, minutePart)
    }

    private fun getTravelTimeInHours(travelTime: String): Double {
        // Remove extra spaces and convert to lowercase for easier parsing
        val formattedTravelTime = travelTime.toLowerCase().replace("\\s+".toRegex(), "")

        // Initialize variables for hours and minutes
        var hours = 0
        var minutes = 0

        // Use regular expressions to extract hours and minutes
        val hoursPattern = Pattern.compile("(\\d+)hour")
        val minutesPattern = Pattern.compile("(\\d+)min")

        val hoursMatcher = hoursPattern.matcher(formattedTravelTime)
        val minutesMatcher = minutesPattern.matcher(formattedTravelTime)

        // Find and add hours if present
        if (hoursMatcher.find()) {
            hours += hoursMatcher.group(1).toInt()
        }

        // Find and add minutes if present
        if (minutesMatcher.find()) {
            minutes += minutesMatcher.group(1).toInt()
        }

        // Convert the total time to hours (1 hour = 1.0, 30 minutes = 0.5)
        return hours.toDouble() + (minutes.toDouble() / 60.0)
    }
}
