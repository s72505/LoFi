package com.lofi.servlet;

import com.lofi.dao.UserDAO;
import com.lofi.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileUpdateServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ProfileUpdateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        /* -------- authentication -------- */
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("login.jsp");
            return;
        }
        int userId = (Integer) session.getAttribute("userId");

        /* -------- read & validate -------- */
        String name  = req.getParameter("name");
        String phone = req.getParameter("phone");

        if (name == null || name.isBlank() ||
            phone == null || phone.isBlank()) {
            res.sendRedirect("profile.jsp?err=missing");
            return;
        }

        /* -------- fetch current user -------- */
        User u;
        try {
            u = UserDAO.findById(userId);
            if (u == null) {                     // should never happen
                res.sendRedirect("login.jsp");
                return;
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "DB lookup failed", ex);
            throw new ServletException(ex);
        }

        /* -------- update & persist -------- */
        u.setName (name.trim());
        u.setPhone(phone.trim());

        try {
            UserDAO.updateProfile(u);            // only name & phone
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Profile update failed", ex);
            throw new ServletException(ex);
        }

        /* -------- keep session in-sync -------- */
        session.setAttribute("name",  u.getName());
        session.setAttribute("phone", u.getPhone());

        /* -------- done -------- */
        res.sendRedirect("profile.jsp?ok=1");
    }

    /* GET → just bounce back */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.sendRedirect("profile.jsp");
    }
}