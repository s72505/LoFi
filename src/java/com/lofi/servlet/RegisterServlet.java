package com.lofi.servlet;

import com.lofi.dao.DBHelper;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

/**
 * Handles new user registration for the LoFi application.
 */
public class RegisterServlet extends HttpServlet {

    /**
     * POST: Processes user registration form submission.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Set character encoding to support UTF-8 inputs
        req.setCharacterEncoding("UTF-8");

        // --- Read form inputs ---
        String name  = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String pass  = req.getParameter("password");
        String role  = req.getParameter("role");

        boolean success = false;  // Track whether registration is successful

        try (Connection c = DBHelper.getConnection()) {

            // --- Check if email already exists ---
            PreparedStatement ck = c.prepareStatement(
                "SELECT 1 FROM users WHERE email=?"
            );
            ck.setString(1, email);
            if (!ck.executeQuery().next()) {

                // --- Email is unique: proceed with insert ---
                PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO users(name, email, phone, pw_hash, role) VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                // Set form values and hash the password
                ins.setString(1, name);
                ins.setString(2, email);
                ins.setString(3, phone);
                ins.setString(4, DigestUtils.sha256Hex(pass));
                ins.setString(5, role);

                // Execute insert
                ins.executeUpdate();
                success = true;

                // --- Log the newly generated user ID (for debugging or logging) ---
                ResultSet k = ins.getGeneratedKeys();
                if (k.next()) {
                    int newId = k.getInt(1);
                    System.out.println("New user auto-ID = " + newId);
                }
            }

        } catch (SQLException e) {
            throw new ServletException(e); // Propagate DB errors to container
        }

        // --- Redirect or forward based on outcome ---
        if (success) {
            res.sendRedirect("login.jsp?msg=registered");
        } else {
            req.setAttribute("err", "E-mail already exists.");
            req.getRequestDispatcher("register.jsp").forward(req, res);
        }
    }
}
