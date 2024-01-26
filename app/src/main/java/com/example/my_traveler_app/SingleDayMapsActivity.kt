package com.example.my_traveler_app

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException

class SingleDayMapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var eventAddresses: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        // Get the list of event addresses from the intent
        eventAddresses = intent.getStringArrayListExtra("eventAddresses")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val addresses = eventAddresses

        if (addresses != null && addresses.isNotEmpty()) {
            // Create a Geocoder instance to convert addresses to LatLng
            val geocoder = Geocoder(this)

            for (i in 0 until addresses.size - 1) {
                val startAddress = addresses[i]
                val endAddress = addresses[i + 1]

                try {
                    val startLocation = geocoder.getFromLocationName(startAddress, 1)
                    val endLocation = geocoder.getFromLocationName(endAddress, 1)

                    if (startLocation != null && startLocation.isNotEmpty() &&
                        endLocation != null && endLocation.isNotEmpty()) {

                        val startLatLng = LatLng(startLocation[0].latitude, startLocation[0].longitude)
                        val endLatLng = LatLng(endLocation[0].latitude, endLocation[0].longitude)

                        // Add a marker for the start and end locations
                        mMap.addMarker(MarkerOptions().position(startLatLng).title(startAddress))
                        mMap.addMarker(MarkerOptions().position(endLatLng).title(endAddress))

                        // Draw a polyline to connect the start and end locations
                        mMap.addPolyline(PolylineOptions()
                            .add(startLatLng, endLatLng)
                            .color(Color.BLUE)
                        )

                        // Calculate and display the estimated travel time (distance) between locations
                        val distance = distanceBetween(startLatLng, endLatLng)
                        mMap.addMarker(
                            MarkerOptions().position(endLatLng)
                                .title("Estimated Travel Time:")
                                .snippet(formatDistance(distance))
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            // Move the camera to the first location
            val firstAddress = addresses.first()
            val firstLocation = geocoder.getFromLocationName(firstAddress, 1)
            if (firstLocation != null && firstLocation.isNotEmpty()) {
                val firstLatLng = LatLng(firstLocation[0].latitude, firstLocation[0].longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10f))
            }
        } else {
            Toast.makeText(this, "No addresses available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun distanceBetween(start: LatLng, end: LatLng): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
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
