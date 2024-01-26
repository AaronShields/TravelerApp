package com.example.my_traveler_app;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.my_traveler_app.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<String> allAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the addresses from the intent
        Intent intent = getIntent();
        allAddresses = intent.getStringArrayListExtra("allAddresses");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (allAddresses != null && allAddresses.size() >= 3) {
            // Set the origin, destination, and waypoint addresses
            String address1 = allAddresses.get(0);
            String address2 = allAddresses.get(1);
            String address3 = allAddresses.get(2);

            // Convert addresses to LatLng (You may use Geocoding API if necessary)
            LatLng origin = getLocationFromAddress(address1);
            LatLng destination = getLocationFromAddress(address2);
            LatLng waypoint = getLocationFromAddress(address3);

            if (origin != null && destination != null && waypoint != null) {
                // Add markers for origin, destination, and waypoint
                mMap.addMarker(new MarkerOptions().position(origin).title("Origin"));
                mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
                mMap.addMarker(new MarkerOptions().position(waypoint).title("Waypoint"));

                // Zoom to fit all markers on the map
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(destination);
                builder.include(waypoint);
                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }
    }

    // Utility method to get LatLng from address (You may implement this method)
    private LatLng getLocationFromAddress(String address) {
        // Implement logic to convert address to LatLng using Geocoding API
        // Return LatLng object or null if not found
        return null;
    }
}