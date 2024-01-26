package com.example.my_traveler_app

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import android.location.Geocoder
import java.io.IOException
import android.graphics.Color
import android.location.Location
class MultiDayMapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var eventAddressesList: ArrayList<ArrayList<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        // Get the list of event addresses for both days from the intent
        eventAddressesList = intent.getSerializableExtra("eventAddressesList") as? ArrayList<ArrayList<String>>

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val eventAddressesList = eventAddressesList

        if (eventAddressesList != null && eventAddressesList.size >= 2) {
            for (day in 0 until eventAddressesList.size) {
                val addresses = eventAddressesList[day]
                val polylineColor = if (day == 0) Color.BLUE else Color.RED

                if (addresses != null && addresses.isNotEmpty()) {
                    // Create a Geocoder instance to convert addresses to LatLng
                    val geocoder = Geocoder(this)
                    var previousLocation: LatLng? = null

                    for (address in addresses) {
                        try {
                            val locations = geocoder.getFromLocationName(address, 1)
                            if (locations?.isNotEmpty() == true) {
                                val location = LatLng(locations[0].latitude, locations[0].longitude)

                                // Add a marker for the current location
                                mMap.addMarker(MarkerOptions().position(location).title(address))

                                if (previousLocation != null) {
                                    // Draw a polyline to connect the previous location and the current location
                                    mMap.addPolyline(PolylineOptions()
                                        .add(previousLocation, location)
                                        .color(polylineColor)
                                    )

                                    // Calculate and display the estimated travel time (distance) between locations
                                    val distance = distanceBetween(previousLocation, location)
                                    mMap.addMarker(
                                        MarkerOptions().position(location)
                                            .title("Day ${day + 1} Estimated Travel Time:")
                                            .snippet(formatDistance(distance))
                                    )
                                }

                                previousLocation = location
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    // Move the camera to the last location of each day
                    if (previousLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(previousLocation, 10f))
                    }
                }
            }
        }
    }

    private fun distanceBetween(start: LatLng, end: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude, results
        )
        return results[0]
    }

    private fun formatDistance(distance: Float): String {
        val km = distance / 1000
        return String.format("%.2f km", km)
    }
}
