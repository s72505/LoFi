package com.lofi.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a user in the system (can be customer, vendor, or admin).
 */
public class User {
    // === Fields ===
    private int userId;         // user_id in the database
    private String name;        // user's full name
    private String email;       // user's email (used for login)
    private String phone;       // user's phone number
    private String pwHash;      // hashed password using SHA-256
    private String role;        // user role: "customer", "vendor", or "admin"

    // === Constructors ===

    // Default constructor (needed for frameworks like JDBC, Jackson, etc.)
    public User() {}

    // Full constructor for initializing all fields
    public User(int userId, String name, String email,
                String phone, String pwHash, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pwHash = pwHash;
        this.role = role;
    }

    // === Getters and Setters ===

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // === Utility Methods ===

    /**
     * Hashes a plain-text password using SHA-256.
     * @param plain the plain password input
     * @return hashed value as a hex string
     */
    public static String sha256(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plain.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hexadecimal string
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Should never happen if Java supports SHA-256
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /**
     * Compares a raw password input with the stored hash.
     * @param plain plain-text password to validate
     * @return true if match, false otherwise
     */
    public boolean matchesPassword(String plain) {
        return pwHash != null && pwHash.equals(sha256(plain));
    }
}
