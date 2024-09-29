// NavActivity.java

package com.s22009961.toursyncer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private SharedPreferences sharedPreferences;
    private Home homeFragment;
    private String emailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Retrieve EmailID from Intent
        Intent intent = getIntent();
        emailID = intent.getStringExtra("EmailID");
        Log.v("NavActivity", String.valueOf(emailID));

        // Initialize the Home fragment with EmailID
        homeFragment = new Home();
        Bundle bundle = new Bundle();
        bundle.putString("EmailID", emailID);
        homeFragment.setArguments(bundle);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemID = item.getItemId();

                if (itemID == R.id.navLocator) {
                    loadFragment(new Locator());
                    return true;
                } else if (itemID == R.id.navTripPlanner) {

                    TripPlannerFragment tripPlannerFragment = new TripPlannerFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("email", emailID);

                    tripPlannerFragment.setArguments(bundle);

                    // Load the fragment with the bundle
                    loadFragment(tripPlannerFragment);
                    return true;
                } else if (itemID == R.id.navHome) {
                    loadFragment(new Home());
                    return true;
                } else if (itemID == R.id.navJournal) {
                    JournalFragment journalFragment = new JournalFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("email", emailID);

                    journalFragment.setArguments(bundle);

                    loadFragment(journalFragment);
                    return true;
                } else if (itemID == R.id.navGallery) {
                    loadFragment(new Gallery());
                    return true;
                }
                return false;
            }
        });

        // Check user logged in
        if (isLoggedIn()) {
            loadFragment(new Home());
            bottomNavigationView.setSelectedItemId(R.id.navHome);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    // Method to check user logged in
    private boolean isLoggedIn() {
        // Retrieve stored user data
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        // Check email and password logged in
        return !email.isEmpty() && !password.isEmpty();
    }
}
