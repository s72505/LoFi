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
        // Get the current session, but don't create a new one
        HttpSession session = request.getSession(false);

        // ===== Security Check: Ensure a customer is logged in =====
        if (session == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // ===== Get form inputs from the POST request =====
            int spotId = Integer.parseInt(request.getParameter("spotId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            int userId = (Integer) session.getAttribute("userId");

            // ===== Input validation: Ensure rating is 1–5 and comment isn't blank =====
            if (comment == null || comment.isBlank() || rating < 1 || rating > 5) {
                response.sendRedirect("MenuServlet?id=" + spotId + "&error=InvalidInput");
                return;
            }

            // ===== Construct Review object to represent the new review =====
            Review review = new Review();
            review.setSpotId(spotId);
            review.setUserId(userId);
            review.setRating(rating);
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());

            // ===== Persist the review to the database =====
            ReviewDAO.addReview(review);

            // ===== AVERAGE RATING UPDATE LOGIC IMPLEMENTED HERE =====

            // Step 1: Calculate the new average rating for the spot
            double averageRating = ReviewDAO.calculateAverageRating(spotId);

            // Step 2: Update the spot's rating in the main food_spots table
            FoodSpotDAO.updateRating(spotId, averageRating);

            // ===== Redirect back to the menu page to reflect the new review =====
            response.sendRedirect("MenuServlet?id=" + spotId);

        } catch (NumberFormatException e) {
            // Handle invalid number formats for inputs
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input format.");
        } catch (SQLException e) {
            // Handle database exceptions and forward to error page
            throw new ServletException("Database error while adding review.", e);
        }
    }
}
