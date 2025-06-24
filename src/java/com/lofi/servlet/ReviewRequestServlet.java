package com.lofi.servlet;

import com.lofi.dao.AdminDAO;
import com.lofi.model.FoodSpotApproval;
import com.lofi.model.MenuApproval;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to handle both GET and POST requests for viewing details of a specific food spot
 * submission pending admin review. Used by adminSummary.jsp.
 */
public class ReviewRequestServlet extends HttpServlet {

    // Delegate GET to process()
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        process(req, res);
    }

    // Delegate POST to process()
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        process(req, res);
    }

    /**
     * Handles the core logic for loading the submission details and forwarding to summary view.
     */
    private void process(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // ---------- Access Control: only admin can proceed ----------
        HttpSession session = req.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            res.sendRedirect("login.jsp?err=loginRequired");
            return;
        }

        // ---------- Extract request ID from parameters ----------
        int requestId;
        try {
            requestId = Integer.parseInt(req.getParameter("requestID"));
        } catch (Exception e) {
            res.sendRedirect("adminDashboard.jsp?err=badId");  // Invalid or missing ID
            return;
        }

        // ---------- Fetch data from database ----------
        try {
            FoodSpotApproval hdr = AdminDAO.findFoodSpotApproval(requestId);  // Fetch main food spot details
            if (hdr == null) {
                res.sendRedirect("adminDashboard.jsp");  // Not found, just return to dashboard
                return;
            }

            List<MenuApproval> menus = AdminDAO.listMenuApproval(requestId);  // Fetch menu items submitted with it

            // ---------- Store all retrieved data in session for rendering ----------
            // Note: naming is consistent with adminSummary.jsp expectations
            session.setAttribute("requestID",       hdr.getRequest_id());
            session.setAttribute("restaurantName",  hdr.getRestaurant_name());
            session.setAttribute("address",         hdr.getAddress());
            session.setAttribute("googleMapsURL",   hdr.getMaps_url());
            session.setAttribute("photoURL",        hdr.getPhoto_url());
            session.setAttribute("openHours",       hdr.getOpen_hours());
            session.setAttribute("closedHours",     hdr.getClosed_hours());
            session.setAttribute("halalCertified",  hdr.getHalal_flag());
            session.setAttribute("workingDays",     hdr.getWorking_days());
            session.setAttribute("menus",           menus);  // List<MenuApproval> for dynamic display

            // ---------- Redirect to summary JSP view ----------
            res.sendRedirect("adminSummary.jsp");

        } catch (SQLException ex) {
            // Forward error to container-defined error handler
            throw new ServletException(ex);
        }
    }
}
