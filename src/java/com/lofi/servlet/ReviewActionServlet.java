package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.dao.ReviewDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles edit and delete actions for reviews submitted by users.
 * Requires the user to be authenticated and the review to belong to the user.
 */
public class ReviewActionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Check for valid session (user must be logged in) ---
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");  // Redirect to login if not authenticated
            return;
        }

        // --- Extract required parameters ---
        String action = request.getParameter("action");  // "edit" or "delete"
        int userId = (Integer) session.getAttribute("userId");
        int spotId = Integer.parseInt(request.getParameter("spotId"));
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        try {
            // --- Delete review ---
            if ("delete".equals(action)) {
                // DAO enforces user ownership check
                ReviewDAO.deleteReview(reviewId, userId);

            // --- Edit review ---
            } else if ("edit".equals(action)) {
                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = request.getParameter("comment");
                // DAO handles ownership validation and performs the update
                ReviewDAO.updateReview(reviewId, userId, rating, comment);
            }

            // --- Update average rating after any modification ---
            double averageRating = ReviewDAO.calculateAverageRating(spotId);
            FoodSpotDAO.updateRating(spotId, averageRating);

            // --- Redirect back to the menu page to show changes ---
            response.sendRedirect("MenuServlet?id=" + spotId);

        } catch (SQLException e) {
            // If any DB-related error occurs, wrap and throw
            throw new ServletException("Database error during review action.", e);
        }
    }
}
