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

    private static final Logger LOG = Logger.getLogger(ChangePasswordServlet.class.getName());

    /* ----------  POST  ---------- */
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

        /* -------- read parameters -------- */
        String curPwd = req.getParameter("currentPwd");
        String newPwd = req.getParameter("newPwd");
        String cfmPwd = req.getParameter("confirmPwd");

        if (curPwd == null || newPwd == null || cfmPwd == null ||
            curPwd.isBlank() || newPwd.isBlank() || cfmPwd.isBlank()) {
            res.sendRedirect("profile.jsp?pwdErr=missing");
            return;
        }

        if (!newPwd.equals(cfmPwd)) {
            res.sendRedirect("profile.jsp?pwdErr=mismatch");
            return;
        }

        if (newPwd.length() < 6) {
            res.sendRedirect("profile.jsp?pwdErr=short");
            return;
        }

        try {
            /* -------- pull current user -------- */
            User u = UserDAO.findById(userId);
            if (u == null || !u.matchesPassword(curPwd)) {
                res.sendRedirect("profile.jsp?pwdErr=wrong");
                return;
            }

            /* -------- new == old ? -------- */
            if (curPwd.equals(newPwd)) {                 // same as current
                res.sendRedirect("profile.jsp?pwdErr=same");
                return;
            }

            /* -------- update -------- */
            String newHash = User.sha256(newPwd);
            UserDAO.updatePassword(userId, newHash);

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Password update failed", ex);
            throw new ServletException(ex);
        }

        /* -------- success -------- */
        res.sendRedirect("profile.jsp?pwdOk=1");
    }

    /* ----------  GET  ---------- */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.sendRedirect("profile.jsp");
    }
}
    