package com.lofi.servlet;

import com.lofi.dao.DBHelper;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

/**
 * Handles user login for the LoFi system.
 * GET  → Shows login form.
 * POST → Authenticates user and sets up session.
 */
public class LoginServlet extends HttpServlet {

    /* ----------------  GET: just show the form  ---------------- */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Simply forward to the login page (login.jsp)
        req.getRequestDispatcher("login.jsp").forward(req, res);
    }

    /* ----------------  POST: authenticate  ---------------- */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8"); // Ensure UTF-8 encoding for input handling

        // Retrieve form parameters
        String email = req.getParameter("email");
        String pwd   = req.getParameter("password");
        String role  = req.getParameter("role");

        boolean ok = false; // Flag to track if login is successful

        /* we also need the phone-number column for the profile page */
        String sql = """
                SELECT user_id, name, phone, pw_hash
                  FROM users
                 WHERE email = ? AND role = ?""";

        try (
            // Get database connection and prepare query
            Connection c = DBHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, email); // Set email in query
            ps.setString(2, role);  // Set role in query

            ResultSet rs = ps.executeQuery();

            // Check if credentials match
            if (rs.next() && DigestUtils.sha256Hex(pwd).equals(rs.getString("pw_hash"))) {

                ok = true; // Login successful

                // Create or reuse session
                HttpSession session = req.getSession(true);
                session.setAttribute("userId", rs.getInt("user_id"));
                session.setAttribute("role",   role);

                /* ─── new attributes used by profile.jsp ─── */
                session.setAttribute("name",  rs.getString("name"));    // for display
                session.setAttribute("email", email);                   // reuse input
                session.setAttribute("phone", rs.getString("phone"));   // fetched from DB
                session.setAttribute("userName", rs.getString("name")); // for backward compatibility
            }

        } catch (SQLException e) {
            // Handle SQL error gracefully
            throw new ServletException(e);
        }

        if (ok) {
            // Redirect to homepage after successful login
            res.sendRedirect("Home");
        } else {
            // Show error message and reload login page
            req.setAttribute("err", "Invalid e-mail or password.");
            req.getRequestDispatcher("login.jsp").forward(req, res);
        }
    }

    @Override
    public String getServletInfo() {
        return "Authenticates users for LoFi";
    }
}
