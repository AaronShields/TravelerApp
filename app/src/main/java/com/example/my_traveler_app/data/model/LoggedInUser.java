package com.example.my_traveler_app.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String email; // Add the email field

    public LoggedInUser(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = this.email; // Initialize the email field
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
