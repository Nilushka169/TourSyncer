package com.s22009961.toursyncer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends Fragment {

    ImageView weather, compass;
    Button try_it;
    ImageView profile;
    private String email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        weather = view.findViewById(R.id.weather);
        compass = view.findViewById(R.id.compass);
        try_it = view.findViewById(R.id.try_it);
        profile = view.findViewById(R.id.profile);


        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Temperature.class);
                startActivity(intent);
            }
        });

        compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Compass.class);
                startActivity(intent);
            }
        });

        try_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find the BottomNavigationView
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavView);

                // Simulate a click on the menu item that corresponds to the TripPlannerFragment
                bottomNavigationView.setSelectedItemId(R.id.navTripPlanner);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ItemCardComingSoon cardAdapter = new ItemCardComingSoon(5);  // 5 cards
        recyclerView.setAdapter(cardAdapter);

        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the action bar
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }



}
