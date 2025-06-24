package com.lofi.servlet;

import com.lofi.dao.FavouriteDAO;
import com.lofi.model.FoodSpot;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // ----- customer: load favourites --------------------------------------------------
        if ("customer".equals(req.getSession().getAttribute("role"))) {
            // Get user ID from session
            int uid = (int) req.getSession().getAttribute("userId");

            try {
                // Retrieve full list of favourites from DAO
                List<FoodSpot> favs = FavouriteDAO.listByUser(uid);

                // Shuffle the list to randomize order
                Collections.shuffle(favs);

                // Select at most 3 favourites for quick-access display
                List<FoodSpot> quickFavs = favs.stream()
                                               .limit(3)
                                               .collect(Collectors.toList());

                // Set attributes for use in JSP view
                req.setAttribute("quickFavs", quickFavs);  // Quick-access preview cards
                req.setAttribute("favs", favs);            // Full favourite list (optional)

            } catch (SQLException ex) {
                // Log any SQL errors that occur
                Logger.getLogger(HomeServlet.class.getName())
                      .log(Level.SEVERE, null, ex);
            }
        }

        // ----- forward to view ------------------------------------------------------------
        // Forward the request to the home page view (home.jsp)
        req.getRequestDispatcher("home.jsp").forward(req, res);
    }
}
