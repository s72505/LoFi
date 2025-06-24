package com.lofi.servlet;

import com.lofi.dao.UserDAO;
import com.lofi.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles updates to a user's profile (name and phone).
 */
public class ProfileUpdateServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ProfileUpdateServlet.class.getName());

    /**
     * POST: Process the submitted profile changes.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // -------- Step 1: Check authentication --------
        HttpSession session = req.getSession(false); // Don’t create new session
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("login.jsp");
            return;
        }
        int userId = (Integer) session.getAttribute("userId");

        // -------- Step 2: Read form data & validate --------
        String name  = req.getParameter("name");
        String phone = req.getParameter("phone");

        if (name == null || name.isBlank() || phone == null || phone.isBlank()) {
            res.sendRedirect("profile.jsp?err=missing");
            return;
        }

        // -------- Step 3: Fetch current user --------
        User u;
        try {
            u = UserDAO.findById(userId);
            if (u == null) {
                res.sendRedirect("login.jsp"); // Defensive: Should not happen
                return;
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "DB lookup failed", ex);
            throw new ServletException(ex);
        }

        // -------- Step 4: Update user object & persist to DB --------
        u.setName(name.trim());
        u.setPhone(phone.trim());

        try {
            UserDAO.updateProfile(u); // Only name and phone
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Profile update failed", ex);
            throw new ServletException(ex);
        }

        // -------- Step 5: Keep session attributes in sync --------
        session.setAttribute("name", u.getName());
        session.setAttribute("phone", u.getPhone());

        // -------- Step 6: Redirect to confirm success --------
        res.sendRedirect("profile.jsp?ok=1");
    }

    /**
     * GET: Just redirect to profile page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.sendRedirect("profile.jsp");
    }
}
