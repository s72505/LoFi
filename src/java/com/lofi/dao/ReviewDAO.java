package com.lofi.dao;

import com.lofi.model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ReviewDAO {

    // Private constructor to prevent instantiation
    private ReviewDAO() {}

    /**
     * Fetches all reviews for a specific food spot.
     * Joins with the users table to retrieve the reviewer's name.
     */
    public static List<Review> getReviewsBySpotId(int spotId) throws SQLException {
        String sql = "SELECT r.*, u.name FROM reviews r JOIN users u ON r.user_id = u.user_id WHERE r.spot_id = ? ORDER BY r.created_at DESC";
        List<Review> reviews = new ArrayList<>();

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Construct Review object from each row
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setSpotId(rs.getInt("spot_id"));
                    review.setUserId(rs.getInt("user_id"));
                    review.setRating(rs.getInt("rating"));
                    review.setComment(rs.getString("comment"));
                    review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    review.setUserName(rs.getString("name"));
                    reviews.add(review);
                }
            }
        }
        return reviews;
    }

    /**
     * Adds a new review into the database.
     * Uses INSERT IGNORE to avoid duplicate primary keys (if applicable).
     */
    public static void addReview(Review review) throws SQLException {
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

    /**
     * Calculates the average rating of a food spot.
     * Returns 0.0 if there are no reviews.
     */
    public static double calculateAverageRating(int spotId) throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE spot_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        }
        return 0.0; // Default value if no rating found
    }

    // ========== NEW METHODS FOR EDIT/DELETE FUNCTIONALITY ==========

    /**
     * Deletes a review for a spot.
     * Only the user who created the review can delete it.
     */
    public static void deleteReview(int reviewId, int userId) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id = ? AND user_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates a review's rating and comment.
     * Only allowed if the user is the original reviewer.
     * Updates the timestamp to NOW.
     */
    public static void updateReview(int reviewId, int userId, int rating, String comment) throws SQLException {
        String sql = "UPDATE reviews SET rating = ?, comment = ?, created_at = NOW() WHERE review_id = ? AND user_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, rating);
            ps.setString(2, comment);
            ps.setInt(3, reviewId);
            ps.setInt(4, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Checks if the given user has already submitted a review for a spot.
     * Useful for preventing multiple reviews per user per spot.
     */
    public static boolean hasUserReviewed(int userId, int spotId) throws SQLException {
        String sql = "SELECT 1 FROM reviews WHERE user_id = ? AND spot_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if review exists
            }
        }
    }
}
