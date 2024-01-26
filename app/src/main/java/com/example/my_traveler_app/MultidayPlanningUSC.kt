package com.example.my_traveler_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.my_traveler_app.UscEvent.EventData
import java.util.ArrayList
import java.util.regex.Pattern

class MultidayPlanningUSC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiday_planning_usc)

        // Access the TextView for displaying selected events
        val eventListTextView: TextView = findViewById(R.id.eventListTextView)

        // Access the Button for viewing the map
        val mapButton: Button = findViewById(R.id.mapButton)

        var clock: Double = 9.0
        // Regular expression pattern to match hours like "7am"
        val pattern = Pattern.compile("(\\d+)am")

        // Create a list to store the addresses
        val eventAddresses = ArrayList<String>()

        // Get the list of selected event names from the intent
        val selectedEventNames = intent.getStringArrayListExtra("selectedEventNames") ?: ArrayList()
        val selectedEventInfo = intent.getParcelableArrayListExtra<EventData>("selectedEventInfo") ?: ArrayList()

        // Flag to check if "Day 2" has been added
        var day2Added = false

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

                // Add 30 minutes for expected event time
                clock += 0.5

                // Add travel time to the clock
                val travelTime = eventData.travelTime
                clock += getTravelTimeInHours(travelTime)

                // Calculate the elapsed time
                val endTime = clock
                var elapsedTime = endTime - startTime

                // Check if "Day 2" needs to be added
                if (!day2Added && (clock > 21.0 || i == selectedEventNames.size - 1)) {
                    // Reset the clock to 9:00 AM and print "Day 2"
                    clock = 9.0
                    eventListTextView.append("\nDay 2\n")
                    day2Added = true
                }

                // Check if end time exceeds 21:00 (9:00 PM) and elapsed time is within 1.5 hours
                if (endTime <= 21.0 && elapsedTime <= 1.5) {
                    eventListTextView.append("$eventName\nStart Time: ${formatTime(startTime)}\nElapsed Time: ${formatTime(elapsedTime)}\nEnd Time: ${formatTime(endTime)}\n\n")

                    // Add the event address to the list
                    eventAddresses.add(eventData.address)

                    // Set the start time for the next event (30 minutes after the current end time)
                    clock = endTime + 0.5
                } else {
                    // If elapsed time exceeds 1.5 hours or end time exceeds 9:00 PM, adjust to 1.5 hours
                    eventListTextView.append("$eventName\nStart Time: ${formatTime(startTime)}\nElapsed Time: 1.50 hours\nEnd Time: ${formatTime(startTime + 1.5)}\n\n")

                    // Adjust elapsed time to 1.5 hours
                    elapsedTime = 1.5

                    // Set the start time for the next event (30 minutes after the adjusted end time)
                    clock = startTime + 1.5 + 0.5
                }
            }
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
