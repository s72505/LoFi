package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.dao.ReviewDAO;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

public class ReviewActionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        int userId = (Integer) session.getAttribute("userId");
        int spotId = Integer.parseInt(request.getParameter("spotId"));
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        try {
            if ("delete".equals(action)) {
                // The DAO method ensures user can only delete their own review
                ReviewDAO.deleteReview(reviewId, userId);

            } else if ("edit".equals(action)) {
                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = request.getParameter("comment");
                // The DAO method ensures user can only update their own review
                ReviewDAO.updateReview(reviewId, userId, rating, comment);
            }
            
            // After any action, recalculate and update the average rating
            double averageRating = ReviewDAO.calculateAverageRating(spotId);
            FoodSpotDAO.updateRating(spotId, averageRating);

            // Redirect back to the menu page to see the changes
            response.sendRedirect("MenuServlet?id=" + spotId);

        } catch (SQLException e) {
            throw new ServletException("Database error during review action.", e);
        }
    }
}