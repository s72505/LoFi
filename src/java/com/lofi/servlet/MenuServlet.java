package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.dao.ReviewDAO;
import com.lofi.model.FoodSpot;
import com.lofi.model.MenuItem;
import com.lofi.model.Review;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This servlet handles the display of a specific food spot's menu and reviews.
 * - It retrieves the spot's basic info, menu items, and user reviews.
 * - It checks if the currently logged-in user has already submitted a review.
 */
public class MenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        try {
            // Parse the spot ID from request parameter (e.g., menu.jsp?id=5)
            int spotId = Integer.parseInt(request.getParameter("id"));

            // Retrieve the food spot's basic info
            FoodSpot spot = FoodSpotDAO.find(spotId);

            // Get all menu items for this food spot
            List<MenuItem> menuItems = FoodSpotDAO.listMenuItemsBySpotId(spotId);

            // Fetch all reviews written for this food spot
            List<Review> reviews = ReviewDAO.getReviewsBySpotId(spotId);

            // Default: assume user has not reviewed yet
            boolean userHasReviewed = false;

            // Get the session, but don’t create a new one if it doesn’t exist
            HttpSession session = request.getSession(false);

            // If a user is logged in, check whether they've already reviewed
            if (session != null && session.getAttribute("userId") != null) {
                int userId = (Integer) session.getAttribute("userId");
                userHasReviewed = ReviewDAO.hasUserReviewed(userId, spotId);
            }

            // If the food spot exists, forward to menu.jsp with data
            if (spot != null) {
                request.setAttribute("spot", spot);
                request.setAttribute("menuItems", menuItems);
                request.setAttribute("reviews", reviews);
                request.setAttribute("userHasReviewed", userHasReviewed); // for UI logic
                request.getRequestDispatcher("menu.jsp").forward(request, response);
            } else {
                // If no such spot found, show 404 error
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Food spot not found.");
            }

        } catch (NumberFormatException e) {
            // Invalid or missing spot ID parameter (e.g., non-numeric)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid food spot ID.");
        } catch (SQLException e) {
            // DB-related error
            throw new ServletException("Database error while fetching menu.", e);
        }
    }
}
