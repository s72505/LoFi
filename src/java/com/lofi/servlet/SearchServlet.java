package com.lofi.servlet;

import com.lofi.dao.FoodSpotDAO;
import com.lofi.model.FoodSpot;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchServlet extends HttpServlet {

    private double parseDouble(String s, double fallback) {
        try {
            return (s != null && !s.isEmpty())
                ? Double.parseDouble(s)
                : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1. Read inputs
        Boolean halal     = "1".equals(req.getParameter("halal")) ? Boolean.TRUE : null;
        double minPrice  = parseDouble(req.getParameter("minPrice"), 0);
        double maxPrice  = parseDouble(req.getParameter("maxPrice"), Double.MAX_VALUE);
        double minRating = parseDouble(req.getParameter("minRating"), 0);

        // 1b. Null‐safe default for sort
        String sortParam = req.getParameter("sort");
        String sort      = (sortParam != null) ? sortParam : ""; 
        // now sort is never null

        // 2. Delegate to DAO
        List<FoodSpot> spots;
        try {
            spots = FoodSpotDAO.findWithFilters(halal, minPrice, maxPrice, minRating, sort);
        } catch (SQLException e) {
            throw new ServletException("Error searching food spots", e);
        }

        // 3. Echo back for the JSP
        req.setAttribute("spots",     spots);
        req.setAttribute("halal",     req.getParameter("halal"));
        req.setAttribute("minPrice",  req.getParameter("minPrice"));
        req.setAttribute("maxPrice",  req.getParameter("maxPrice"));
        req.setAttribute("minRating", req.getParameter("minRating"));
        req.setAttribute("sort",      sort);

        // 4. Forward
        req.getRequestDispatcher("/search.jsp")
           .forward(req, resp);
    }
}
    