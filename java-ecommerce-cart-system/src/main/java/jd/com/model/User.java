package jd.com.model;

import java.io.Serializable;
// Handles user accounts
public class User implements Serializable {
    private final String username;
    private final String passwordHash;
    private final boolean isAdmin;

    public User(String username, String passwordHash, boolean isAdmin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isAdmin() { return isAdmin; }
}