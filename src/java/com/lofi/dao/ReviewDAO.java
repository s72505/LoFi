package com.lofi.dao;

import com.lofi.model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ReviewDAO {

    private ReviewDAO() {}

    /**
     * Fetches all reviews for a specific food spot, joining with the users table
     * to get the reviewer's name.
     * @param spotId The ID of the food spot.
     * @return A list of Review objects.
     */
    public static List<Review> getReviewsBySpotId(int spotId) throws SQLException {
        String sql = "SELECT r.*, u.name FROM reviews r JOIN users u ON r.user_id = u.user_id WHERE r.spot_id = ? ORDER BY r.created_at DESC";
        List<Review> reviews = new ArrayList<>();

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setSpotId(rs.getInt("spot_id"));
                    review.setUserId(rs.getInt("user_id"));
                    review.setRating(rs.getInt("rating"));
                    review.setComment(rs.getString("comment"));
                    review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    review.setUserName(rs.getString("name")); // Set the joined user name
                    reviews.add(review);
                }
            }
        }
        return reviews;
    }

    /**
     * Adds a new review to the database.
     * @param review The Review object to be added.
     */
    public static void addReview(Review review) throws SQLException {
        // A user can only review a spot once, so we use INSERT IGNORE
        // or you could use REPLACE INTO depending on desired behavior.
        String sql = "INSERT IGNORE INTO reviews (spot_id, user_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, review.getSpotId());
            ps.setInt(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, Timestamp.valueOf(review.getCreatedAt()));
            ps.executeUpdate();
        }
    }
        // ========== NEW METHOD TO CALCULATE THE AVERAGE RATING ==========
    public static double calculateAverageRating(int spotId) throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE spot_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // The result of AVG might be null if there are no reviews, so we default to 0.0
                    return rs.getDouble("avg_rating");
                }
            }
        }
        return 0.0; // Return 0 if no reviews are found
    }
}