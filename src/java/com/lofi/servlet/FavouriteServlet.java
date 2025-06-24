package com.lofi.servlet;

import com.lofi.dao.FavouriteDAO;
import com.lofi.model.FoodSpot;

import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all “favourite”-related actions.
 *
 *  GET  → shows every favourited food spot (favourite.jsp)
 *  POST → add / remove a food-spot from favourites, then redirects
 */
//@WebServlet(name = "FavouriteServlet", urlPatterns = {"/FavouriteServlet"})
public class FavouriteServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(FavouriteServlet.class.getName());

    /* ---------- GET: Display the user's list of favourites ---------- */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Retrieve the current user's ID from the session
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        // Redirect to login if not authenticated
        if (userId == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        List<FoodSpot> list;
        try {
            // Fetch all food spots favourited by the user
            list = FavouriteDAO.listByUser(userId);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Unable to load user favourites", ex);
            throw new ServletException(ex); // Forward to error page if DB fails
        }

        // Set favourites list (empty if null) into request scope and forward to view
        req.setAttribute("favs", list == null ? Collections.emptyList() : list);
        req.getRequestDispatcher("favourites.jsp").forward(req, res);
    }

    /* ---------- POST: Add or remove a food spot from favourites ---------- */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get the userId from the session
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        // Redirect to login if not authenticated
        if (userId == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        // Read form parameters
        String action = req.getParameter("action");   // "add" or "del"
        String sid    = req.getParameter("spotId");   // spot ID to be added/removed

        // Validate inputs
        if (action == null || sid == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        int spotId;
        try {
            // Parse spotId into integer
            spotId = Integer.parseInt(sid);
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid spotId");
            return;
        }

        try {
            // Perform the action
            switch (action) {
                case "add" -> FavouriteDAO.add(userId, spotId);    // Add to favourites
                case "del" -> FavouriteDAO.remove(userId, spotId); // Remove from favourites
                default     -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Favourite DB operation failed", ex);
            throw new ServletException(ex);
        }

        // Post-Redirect-Get pattern: redirect back to servlet after POST
        res.sendRedirect(req.getContextPath() + "/FavouriteServlet");
    }
}
