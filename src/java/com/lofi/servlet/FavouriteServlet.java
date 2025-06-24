package com.lofi.servlet;

import com.lofi.dao.FavouriteDAO;
import com.lofi.model.FoodSpot;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
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

    /* ----------  GET : show list  ---------- */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {                       // not logged in → back to login
            res.sendRedirect("login.jsp");
            return;
        }

        List<FoodSpot> list;
        try {
            list = FavouriteDAO.listByUser(userId);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Unable to load user favourites", ex);
            throw new ServletException(ex);        // will be handled by error-page
        }

        req.setAttribute("favs", list == null ? Collections.emptyList() : list);
        req.getRequestDispatcher("favourites.jsp").forward(req, res);
    }

    /* ----------  POST : add / delete  ---------- */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");       // "add" | "del"
        String sid     = req.getParameter("spotId");      // food-spot id

        if (action == null || sid == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        int spotId;
        try {
            spotId = Integer.parseInt(sid);
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid spotId");
            return;
        }

        try {
            switch (action) {
                case "add" -> FavouriteDAO.add(userId, spotId);
                case "del" -> FavouriteDAO.remove(userId, spotId);
                default    -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Favourite DB operation failed", ex);
            throw new ServletException(ex);
        }

        /* back to the list page (PRG pattern) */
        res.sendRedirect(req.getContextPath() + "/FavouriteServlet");
    }
}
