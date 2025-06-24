package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.model.FoodSpot;
import com.lofi.model.MenuItem;
import com.lofi.dao.ReviewDAO;
import com.lofi.model.Review;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MenuServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int spotId = Integer.parseInt(request.getParameter("id"));

            // Fetch the food spot details
            FoodSpot spot = FoodSpotDAO.find(spotId);
            
            // Fetch the list of menu items for that spot
            List<MenuItem> menuItems = FoodSpotDAO.listMenuItemsBySpotId(spotId);

            // ========== CODE TO FETCH REVIEWS IMPLEMENTED HERE ==========
            List<Review> reviews = ReviewDAO.getReviewsBySpotId(spotId);

            if (spot != null) {
                // Set all necessary data as request attributes
                request.setAttribute("spot", spot);
                request.setAttribute("menuItems", menuItems);
                request.setAttribute("reviews", reviews); // Pass the reviews to the JSP
                
                // Forward to the JSP page
                request.getRequestDispatcher("menu.jsp").forward(request, response);
            } else {
                // Handle case where food spot is not found
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Food spot not found.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid food spot ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching menu.", e);
        }
    }
}