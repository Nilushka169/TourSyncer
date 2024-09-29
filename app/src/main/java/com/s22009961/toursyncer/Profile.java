package com.s22009961.toursyncer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView profileImage;
    TextView usernameTextView;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Retrieve user email from SharedPreferences
        userEmail = sharedPreferences.getString("email", "");

        profileImage = findViewById(R.id.profileImage);
        usernameTextView = findViewById(R.id.usernameTextView);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Set username (using email for demonstration purposes)
        usernameTextView.setText(userEmail);

        // Logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the logged in state to false
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Redirect to the sign in
                Intent intent = new Intent(Profile.this, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
