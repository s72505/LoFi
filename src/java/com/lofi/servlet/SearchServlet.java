package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.model.FoodSpot;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles advanced filtering and sorting for food spot search results.
 * Parameters include: cuisine, halal, price range, rating, and sorting.
 */
public class SearchServlet extends HttpServlet {

    /**
     * Utility method to safely parse a String to double with fallback.
     */
    private double parseDouble(String s, double fallback) {
        try {
            return (s != null && !s.isEmpty()) ? Double.parseDouble(s) : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * Handles the HTTP GET request to perform a filtered food-spot search.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ========== 1. INPUT: Read search/filter parameters from request ==========
        String cuisine   = req.getParameter("cuisine");   // e.g. "Noodles", "Hot Food", etc.
        Boolean halal    = "1".equals(req.getParameter("halal")) ? Boolean.TRUE : null; // "1" = filter for Halal only
        double minPrice  = parseDouble(req.getParameter("minPrice"), 0);               // Minimum price (default = 0)
        double maxPrice  = parseDouble(req.getParameter("maxPrice"), Double.MAX_VALUE); // Maximum price (default = unlimited)
        double minRating = parseDouble(req.getParameter("minRating"), 0);              // Minimum rating (default = 0)

        // Default sort = "name_asc" if nothing is selected
        String sortParam = req.getParameter("sort");
        String sort      = (sortParam != null) ? sortParam : "name_asc";

        // ========== 2. DELEGATE: Pass all filters to DAO to retrieve matching spots ==========
        List<FoodSpot> spots;
        try {
            spots = FoodSpotDAO.findWithFilters(cuisine, halal, minPrice, maxPrice, minRating, sort);
        } catch (SQLException e) {
            throw new ServletException("Error searching food spots", e);
        }

        // ========== 3. OUTPUT: Echo back all search/filter values for reuse in JSP ==========
        req.setAttribute("spots", spots);           // Result list
        req.setAttribute("cuisine", cuisine);       // Preserve selected cuisine
        req.setAttribute("halal", req.getParameter("halal"));             // For form checkbox state
        req.setAttribute("minPrice", req.getParameter("minPrice"));       // For form input field
        req.setAttribute("maxPrice", req.getParameter("maxPrice"));
        req.setAttribute("minRating", req.getParameter("minRating"));
        req.setAttribute("sort", sort);             // For sort dropdown

        // ========== 4. VIEW: Forward to results page (results.jsp) ==========
        req.getRequestDispatcher("/results.jsp").forward(req, resp);
    }
}
