package com.lofi.servlet;

import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.*;

/**
 * Handles user logout by ending the session and redirecting to the homepage.
 */
public class LogoutServlet extends HttpServlet {

    /**
     * Handles the HTTP GET method for logging out.
     * - Invalidates the session
     * - Clears cache headers
     * - Redirects to index.html
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get the current session, but don't create a new one if it doesn't exist
        HttpSession session = req.getSession(false);

        // If there's a session, invalidate it to log the user out
        if (session != null) {
            session.invalidate();
        }

        // Prevent caching of the logout response (security best practice)
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache");                                   // HTTP 1.0
        res.setDateHeader("Expires", 0);                                       // Proxies

        // Redirect user to the homepage after logout
        res.sendRedirect("index.html");
    }
}
