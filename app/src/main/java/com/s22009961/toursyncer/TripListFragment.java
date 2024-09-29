package com.s22009961.toursyncer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class TripListFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private ListView listView;
    private String userEmail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        listView = view.findViewById(R.id.trip_list_view);
        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
        }
        if (userEmail != null) {
            loadTrips(userEmail);
        } else {
            // Handle the case where the email is null
            Toast.makeText(getContext(), "User email is null", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
    private void loadTrips(String userEmail) {

        Cursor cursor = dbHelper.getAllTrips(userEmail);
        String[] from = {
                DatabaseHelper.TRIP_COLUMN_TRIP_NAME,
                DatabaseHelper.TRIP_COLUMN_DESTINATION,
                DatabaseHelper.TRIP_COLUMN_START_DAY,
                DatabaseHelper.TRIP_COLUMN_START_MONTH,
                DatabaseHelper.TRIP_COLUMN_START_HOUR,
                DatabaseHelper.TRIP_COLUMN_START_MINUTE,
                DatabaseHelper.TRIP_COLUMN_END_DAY,
                DatabaseHelper.TRIP_COLUMN_END_MONTH,
                DatabaseHelper.TRIP_COLUMN_END_HOUR,
                DatabaseHelper.TRIP_COLUMN_END_MINUTE};
        int[] to = {
                R.id.trip_name,
                R.id.trip_destination,
                R.id.trip_start_date_time,
                R.id.trip_end_date_time};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.trip_list_item,
                cursor,
                from,
                to,
                0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.trip_start_date_time || view.getId() == R.id.trip_end_date_time) {
                    int day = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_START_DAY));
                    int month = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_START_MONTH)) + 1;
                    int hour = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_START_HOUR));
                    int minute = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_START_MINUTE));
                    String startDate = "Start Date - " + day + "/" + month + "   Start Time - " + hour + ":" + (minute < 10 ? "0" + minute : minute);
                    day = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_END_DAY));
                    month = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_END_MONTH)) + 1;
                    hour = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_END_HOUR));
                    minute = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_END_MINUTE));
                    String endDate = "End Date - " + day + "/" + month + "   End Time - " + hour + ":" + (minute < 10 ? "0" + minute : minute);
                    ((TextView) view).setText(view.getId() == R.id.trip_start_date_time ? startDate : endDate);
                    return true;}
                return false;}
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String tripName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TRIP_COLUMN_TRIP_NAME));
                showDeleteConfirmationDialog(tripName);}
        });
    }
    private void showDeleteConfirmationDialog(final String tripName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete this trip?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTrip(tripName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        builder.create().show();}
    private void deleteTrip(String tripName) {
        boolean success = dbHelper.deleteTripByName(tripName);
        if (success) {
            Toast.makeText(getContext(), "Trip deleted successfully", Toast.LENGTH_SHORT).show();
            loadTrips(userEmail);
        } else {
            Toast.makeText(getContext(), "Failed to delete trip", Toast.LENGTH_SHORT).show();}
    }
}
