package com.lofi.servlet;

import com.lofi.dao.UserDAO;
import com.lofi.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangePasswordServlet extends HttpServlet {

    // Logger for error tracking
    private static final Logger LOG = Logger.getLogger(ChangePasswordServlet.class.getName());

    /* ---------- POST method: Handles password change requests ---------- */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        /* -------- Session Validation: Ensure user is logged in -------- */
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("login.jsp");  // If not logged in, redirect to login
            return;
        }
        int userId = (Integer) session.getAttribute("userId"); // Get current user ID

        /* -------- Read Form Parameters -------- */
        String curPwd = req.getParameter("currentPwd");
        String newPwd = req.getParameter("newPwd");
        String cfmPwd = req.getParameter("confirmPwd");

        // Basic null and blank check
        if (curPwd == null || newPwd == null || cfmPwd == null ||
            curPwd.isBlank() || newPwd.isBlank() || cfmPwd.isBlank()) {
            res.sendRedirect("profile.jsp?pwdErr=missing");
            return;
        }

        // New password and confirmation must match
        if (!newPwd.equals(cfmPwd)) {
            res.sendRedirect("profile.jsp?pwdErr=mismatch");
            return;
        }

        // Enforce minimum password length
        if (newPwd.length() < 6) {
            res.sendRedirect("profile.jsp?pwdErr=short");
            return;
        }

        try {
            /* -------- Authenticate current password -------- */
            User u = UserDAO.findById(userId); // Fetch user from DB
            if (u == null || !u.matchesPassword(curPwd)) {
                res.sendRedirect("profile.jsp?pwdErr=wrong"); // If wrong current password
                return;
            }

            /* -------- Prevent reusing the same password -------- */
            if (curPwd.equals(newPwd)) {
                res.sendRedirect("profile.jsp?pwdErr=same"); // Same password entered
                return;
            }

            /* -------- Hash and update the new password -------- */
            String newHash = User.sha256(newPwd); // Hash new password
            UserDAO.updatePassword(userId, newHash); // Save in DB

        } catch (SQLException ex) {
            // Log and propagate database error
            LOG.log(Level.SEVERE, "Password update failed", ex);
            throw new ServletException(ex);
        }

        /* -------- Redirect on success -------- */
        res.sendRedirect("profile.jsp?pwdOk=1");
    }

    /* ---------- GET method: Not used for changing password, redirect to profile ---------- */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.sendRedirect("profile.jsp");
    }
}
