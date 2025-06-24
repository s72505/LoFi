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
            int uid = (int) req.getSession().getAttribute("userId");

            try {
                // full list from DAO
                List<FoodSpot> favs = FavouriteDAO.listByUser(uid);

                // randomise order, then keep at most 3
                Collections.shuffle(favs);
                List<FoodSpot> quickFavs = favs.stream()
                                               .limit(3)
                                               .collect(Collectors.toList());

                // make the result(s) available to JSP
                req.setAttribute("quickFavs", quickFavs);  // for the quick-access block
                // If you still need the complete list somewhere else keep the next line,
                // otherwise you can remove it:
                req.setAttribute("favs", favs);

            } catch (SQLException ex) {
                Logger.getLogger(HomeServlet.class.getName())
                      .log(Level.SEVERE, null, ex);
            }
        }

        // ----- forward to view ------------------------------------------------------------
        req.getRequestDispatcher("home.jsp").forward(req, res);
    }
}
