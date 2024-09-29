package com.s22009961.toursyncer;

import com.google.android.gms.location.LocationRequest;

public class LocationUtils {

    public static LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // Update location every 5 seconds
        locationRequest.setFastestInterval(5000); // The fastest update interval in milliseconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Use high accuracy location updates
        return locationRequest;
    }
}
