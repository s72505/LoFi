package com.lofi.servlet;

import com.lofi.dao.VendorDAO;
import com.lofi.model.FoodSpotApproval;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VendorDashboardServlet extends HttpServlet {

    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        /* ─── Session check: ensure user is a logged-in vendor ─── */
        HttpSession sess = req.getSession(false);
        if (sess == null || !"vendor".equals(sess.getAttribute("role"))) {
            res.sendRedirect("login.jsp?err=loginRequired");
            return;
        }

        // Extract user ID from session
        int userId = (Integer) sess.getAttribute("userId");

        /* ─── Fetch submission data belonging to the logged-in vendor ─── */
        try {
            List<FoodSpotApproval> subs = VendorDAO.listSubmissions(userId);
            req.setAttribute("submissions", subs); // store the data in request for JSP
        } catch (SQLException ex) {
            throw new ServletException(ex); // wrap SQL exception for proper error handling
        }

        // Forward to the vendor dashboard JSP to display submissions
        req.getRequestDispatcher("vendorDashboard.jsp").forward(req, res);
    }

    /* ─── POST is unused: fallback to GET route for safety ─── */
    @Override 
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs)
            throws IOException {
        rs.sendRedirect("VendorDashboardServlet");
    }
}
