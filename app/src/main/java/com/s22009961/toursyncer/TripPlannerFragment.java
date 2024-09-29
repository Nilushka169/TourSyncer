package com.s22009961.toursyncer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
public class TripPlannerFragment extends Fragment {
    private EditText etTripName, etDestination;
    private String userEmail;
    private DatePicker dpStartDate, dpEndDate;
    private TimePicker tpStartTime, tpEndTime;
    private Button btnSaveTrip, btnViewTrips;
    private DatabaseHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_planner, container, false);
        etTripName = view.findViewById(R.id.et_trip_name);
        etDestination = view.findViewById(R.id.et_destination);
        dpStartDate = view.findViewById(R.id.dp_start_date);
        dpEndDate = view.findViewById(R.id.dp_end_date);
        tpStartTime = view.findViewById(R.id.tp_start_time);
        tpEndTime = view.findViewById(R.id.tp_end_time);
        btnSaveTrip = view.findViewById(R.id.btn_save_trip);
        btnViewTrips = view.findViewById(R.id.btn_view_trip);
        dbHelper = new DatabaseHelper(getContext());


        btnSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip();
            }
        });
        btnViewTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTrips();
            }
        });
        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
            Log.d("TripPlannerFragment123", "Received value: " + userEmail);
        }


        return view;
    }


    private void saveTrip() {
        String tripName = etTripName.getText().toString();
        String destination = etDestination.getText().toString();
        int startDay = dpStartDate.getDayOfMonth();
        int startMonth = dpStartDate.getMonth();
        int startYear = dpStartDate.getYear();
        int endDay = dpEndDate.getDayOfMonth();
        int endMonth = dpEndDate.getMonth();
        int endYear = dpEndDate.getYear();
        int startHour = tpStartTime.getCurrentHour();
        int startMinute = tpStartTime.getCurrentMinute();
        int endHour = tpEndTime.getCurrentHour();
        int endMinute = tpEndTime.getCurrentMinute();

        if (tripName.isEmpty() || destination.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isInserted = dbHelper.insertTrip(tripName, destination, startDay, startMonth, startYear,
                endDay, endMonth, endYear, startHour, startMinute, endHour, endMinute, userEmail );
        if (isInserted) {
            Toast.makeText(getActivity(), "Trip Saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Error Saving Trip", Toast.LENGTH_LONG).show();
        }
    }
    private void viewTrips() {
        String userEmail = getArguments().getString("email"); // Retrieve the email
        TripListFragment tripListFragment = new TripListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", userEmail);
        tripListFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, tripListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}