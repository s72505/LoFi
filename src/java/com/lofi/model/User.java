package com.lofi.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private int    userId;     // user_id
    private String name;       // name
    private String email;      // email
    private String phone;      // phone
    private String pwHash;     // pw_hash
    private String role;       // role  (customer | vendor | admin)

    /* --- constructors --- */
    public User() {}

    public User(int userId, String name, String email,
                String phone, String pwHash, String role) {
        this.userId  = userId;
        this.name    = name;
        this.email   = email;
        this.phone   = phone;
        this.pwHash  = pwHash;
        this.role    = role;
    }

    /* ---------- getters / setters ---------- */

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


    /* ---------- utility ---------- */
    public static String sha256(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /** Compare a plain password with the stored hash */
    public boolean matchesPassword(String plain) {
        return pwHash != null && pwHash.equals(sha256(plain));
    }
}
