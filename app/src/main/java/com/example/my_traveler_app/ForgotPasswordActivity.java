package com.example.my_traveler_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailBox, passwordBox, securityQuestionBox;
    private Button changePasswordButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailBox = findViewById(R.id.email_box);
        passwordBox = findViewById(R.id.password_box);
        securityQuestionBox = findViewById(R.id.securityquestion_box);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailBox.getText().toString();
                final String newPassword = passwordBox.getText().toString();
                final String securityAnswer = securityQuestionBox.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(securityAnswer)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean credentialsMatch = false;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String dbEmail = userSnapshot.child("email").getValue(String.class);
                            String dbPassword = userSnapshot.child("password").getValue(String.class);
                            String dbSecurityAnswer = userSnapshot.child("security_question").getValue(String.class);

                            if (dbEmail != null && dbEmail.equals(email)
                                    && dbPassword != null && dbPassword.equals(newPassword)
                                    && dbSecurityAnswer != null && dbSecurityAnswer.equals(securityAnswer)) {
                                credentialsMatch = true;
                                break; // Exit loop if credentials match
                            }
                        }

                        if (credentialsMatch) {
                            // Change password logic here
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ForgotPasswordActivity.this, LandingPage.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Incorrect credentials or email not found
                            Toast.makeText(ForgotPasswordActivity.this, "Invalid email or security answer.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
