package com.s22009961.toursyncer;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
public class Locator extends Fragment {
    private EditText editTextTextStartPoint;
    private EditText editTextTextDestinationPoint;
    private Button btnGetPath;
    private Button btnTrackLiveLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locator, container, false);

        editTextTextStartPoint = view.findViewById(R.id.editTextTextStartPoint);
        editTextTextDestinationPoint = view.findViewById(R.id.editTextTextDestinationPoint);
        btnGetPath = view.findViewById(R.id.btnGetPath);
        btnTrackLiveLocation = view.findViewById(R.id.btnTrackLiveLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        btnGetPath.setOnClickListener(v -> {
            String startingPoint = editTextTextStartPoint.getText().toString();
            String endPoint = editTextTextDestinationPoint.getText().toString();
            if (startingPoint.equals("") || endPoint.equals("")) {
                Toast.makeText(getContext(), "Please enter Start point and Destination point", Toast.LENGTH_SHORT).show();
            } else {
                getPath(startingPoint, endPoint);
            }
        });
        btnTrackLiveLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }trackLiveLocation();
        });
        return view;
    }
    private void getPath(String startingPoint, String endPoint) {
        try {Uri uri = Uri.parse("https://www.google.com/maps/dir/" + startingPoint + "/" + endPoint);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&ql=US");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);}
    }
    @SuppressLint("MissingPermission")
    private void trackLiveLocation() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String uri = "geo:" + latitude + "," + longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);}
            }
        };
        fusedLocationClient.requestLocationUpdates(LocationUtils.getLocationRequest(), locationCallback, null);}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);}
    }
}
