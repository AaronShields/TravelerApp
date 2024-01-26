package com.example.my_traveler_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private EditText emailBox, passwordBox, securityQuestionBox, usernameBox;
    private Button signUpButton;

    private DatabaseReference databaseReference;

    // User class definition
    public static class User {
        private String email;
        private String password;
        private String securityQuestion;
        private String username;

        // Required default constructor
        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String password, String securityQuestion, String username) {
            this.email = email;
            this.password = password;
            this.securityQuestion = securityQuestion;
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSecurityQuestion() {
            return securityQuestion;
        }

        public void setSecurityQuestion(String securityQuestion) {
            this.securityQuestion = securityQuestion;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        emailBox = findViewById(R.id.email_box);
        passwordBox = findViewById(R.id.password_box);
        securityQuestionBox = findViewById(R.id.securityquestion_box);
        usernameBox = findViewById(R.id.usernamebox);

        signUpButton = findViewById(R.id.signupButton);


        databaseReference = FirebaseDatabase.getInstance().getReference();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from EditText fields
                String email = emailBox.getText().toString();
                String password = passwordBox.getText().toString();
                String securityQuestionAnswer = securityQuestionBox.getText().toString();
                String username = usernameBox.getText().toString();

                // Generate a unique user ID for the new user
                String userId = databaseReference.child("users").push().getKey();

                // Create a user object with the collected data
                User newUser = new User(email, password, securityQuestionAnswer, username);

                // Write these values under the generated user ID in Firebase Realtime Database
                databaseReference.child("users").child(userId).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Redirect to landing page or another activity on success
                                Intent intent = new Intent(CreateAccount.this, LandingPage.class);
                                startActivity(intent);
                                finish(); // Optional: Close this activity to prevent going back on pressing back button
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        });
    }}
