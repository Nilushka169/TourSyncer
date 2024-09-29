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
public class SignUp extends AppCompatActivity {
    Button buttonSignUp;
    ImageView imageViewGoogle2, imageViewFb;
    TextView signUpLink;
    EditText editTextFirstName, editTextLastName, editTextEmailAddress, editTextPassword, editTextConfirmPassword;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.
                FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        imageViewGoogle2 = findViewById(R.id.imageViewGoogle2);
        imageViewFb = findViewById(R.id.imageViewFb);
        signUpLink = findViewById(R.id.signUpLink);
        editTextFirstName = findViewById(R.id.editTextTextFirstName);
        editTextLastName = findViewById(R.id.editTextTextLastName);
        editTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextTextConfirmPassword);
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmailAddress.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(SignUp.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (checkUserExists(email)) {
                    Toast.makeText(SignUp.this, "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    saveUser(firstName, lastName, email, password);
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                    finish();}
            }
        });
        imageViewGoogle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Google sign-in activity
//                Intent intent = new Intent(SignUp.this,Home.class);
//                startActivity(intent);
            }
        });
        imageViewFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignUp.this,Home.class);
//                startActivity(intent);

            }
        });
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);}
        });
    }
    private boolean checkUserExists(String email) {
        String existingEmail = sharedPreferences.getString(email, "");
        return !existingEmail.isEmpty();}
    private void saveUser(String firstName, String lastName, String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email, password);
        editor.apply();}
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

}