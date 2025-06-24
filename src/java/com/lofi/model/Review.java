package com.lofi.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a review left by a user for a specific food spot.
 */
public class Review implements Serializable {

    // Unique identifier for this review
    private int reviewId;

    // ID of the food spot this review belongs to
    private int spotId;

    // ID of the user who submitted the review
    private int userId;

    // Numerical rating given by the user (e.g., 1–5 stars)
    private int rating;

    // Optional written feedback from the user
    private String comment;

    // Timestamp for when the review was created
    private LocalDateTime createdAt;

    // The name of the user (fetched via JOIN for display purposes)
    private String userName;

    // ===== Default constructor =====
    public Review() {}

    // ===== Getters and setters for all fields =====

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
