package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.dao.ReviewDAO;
import com.lofi.model.Review;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AddReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Security: Ensure a customer is logged in
        if (session == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int spotId = Integer.parseInt(request.getParameter("spotId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            int userId = (Integer) session.getAttribute("userId");

            // Basic validation
            if (comment == null || comment.isBlank() || rating < 1 || rating > 5) {
                 response.sendRedirect("MenuServlet?id=" + spotId + "&error=InvalidInput");
                 return;
            }

            Review review = new Review();
            review.setSpotId(spotId);
            review.setUserId(userId);
            review.setRating(rating);
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());

            // First, add the new review to the 'reviews' table
            ReviewDAO.addReview(review);

            // ========== AVERAGE RATING UPDATE LOGIC IMPLEMENTED HERE ==========

            // 1. Recalculate the new average rating from all reviews for this spot
            double averageRating = ReviewDAO.calculateAverageRating(spotId);
            
            // 2. Update the 'rating' in the 'food_spots' table with the new average
            FoodSpotDAO.updateRating(spotId, averageRating);
            
            // =================================================================

            // Redirect back to the menu page to see the new review and updated rating
            response.sendRedirect("MenuServlet?id=" + spotId);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input format.");
        } catch (SQLException e) {
            throw new ServletException("Database error while adding review.", e);
        }
    }
}