package com.s22009961.toursyncer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    Button buttonSignIn;
    ImageView imageViewGoogle;
    ImageView imageViewFb;
    TextView signUpLink;
    EditText editTextEmailAddress;
    EditText editTextPassword;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        imageViewGoogle = findViewById(R.id.imageViewGoogle);
        imageViewFb = findViewById(R.id.imageViewFb);
        signUpLink = findViewById(R.id.signUpLink);
        editTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Check user logged in
        if (isLoggedIn()) {
            // If logged in, navigate to nav activity
            Intent intent = new Intent(SignIn.this, NavActivity.class);
            startActivity(intent);
            finish();
        }

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmailAddress.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignIn.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (checkCredentials(email, password)) {
                    // Save the logged in state
                    saveLoggedInState(email, password);


                    Intent intent = new Intent(SignIn.this, NavActivity.class);
                    intent.putExtra("EmailID", email);
                    startActivity(intent);
                    finish();

                } else {
                    // Failed sign-in
                    Toast.makeText(SignIn.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, NavActivity.class);
                startActivity(intent);
            }
        });

        imageViewFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, NavActivity.class);
                startActivity(intent);
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    // Method to check user logged in
    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    // Method to save logged in
    private void saveLoggedInState(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    // Method to check user credentials
    private boolean checkCredentials(String email, String password) {
        String savedPassword = sharedPreferences.getString(email, "");
        return savedPassword.equals(password);
    }
}
