package com.lofi.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Review implements Serializable {
    private int reviewId;
    private int spotId;
    private int userId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private String userName; // To hold the user's name from a JOIN query

    public Review() {}

    // Getters and Setters for all fields
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getSpotId() { return spotId; }
    public void setSpotId(int spotId) { this.spotId = spotId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}