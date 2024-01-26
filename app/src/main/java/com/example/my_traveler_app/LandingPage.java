package com.example.my_traveler_app;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.my_traveler_app.ui.login.LoginResult;
import com.example.my_traveler_app.ui.login.LoginViewModel;
import com.example.my_traveler_app.ui.login.LoginViewModelFactory;

public class LandingPage extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        Button loginButton = findViewById(R.id.loginButton);
        EditText usernameEditText = findViewById(R.id.username_box);
        EditText passwordEditText = findViewById(R.id.password_box);
        TextView errorMessageTextView = findViewById(R.id.errorMessageTextView);

        TextView forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        TextView createAccountLink = findViewById(R.id.createaccount);

        // Set click listener for the forgot password link
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the action you want when the link is clicked
                // For example, navigate to the forgot password activity:
                Intent intent = new Intent(LandingPage.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the action you want when the link is clicked
                // For example, navigate to the forgot password activity:
                Intent intent = new Intent(LandingPage.this, CreateAccount.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();
                Log.d("LoginActivity", "Entered Email: " + enteredUsername);
                Log.d("LoginActivity", "Entered Password: " + enteredPassword);

                // Call the login method in the ViewModel
                loginViewModel.login(enteredUsername, enteredPassword);

                // Observe the login result from the ViewModel
                loginViewModel.getLoginResult().observe(LandingPage.this, new Observer<LoginResult>() {
                    @Override
                    public void onChanged(LoginResult loginResult) {
                        if (loginResult != null && loginResult.getSuccess() != null) {
                            // Authentication successful, navigate to the next activity
                            Intent intent = new Intent(LandingPage.this, Event.class);
                            startActivity(intent);
                        }
                        // If loginResult is null or login was unsuccessful, do nothing and keep them on the main page
                    }
                });

            }
        });
    }
}
