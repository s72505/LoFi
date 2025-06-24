package com.lofi.servlet;

import com.lofi.dao.AdminDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ApprovalDecisionServlet extends HttpServlet {

  // Handles POST requests when admin submits approval/rejection for food spot/menu
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {

    int requestId;

    // ===== Parse and validate the request ID from form =====
    try {
      requestId = Integer.parseInt(req.getParameter("requestID"));
    } catch (Exception e) {
      // If parsing fails, redirect with error flag
      res.sendRedirect("adminDashboard.jsp?err=badId");
      return;
    }

    // ===== Get form inputs =====
    boolean approveSpot = Boolean.parseBoolean(req.getParameter("approveSpot")); // true if spot is approved
    boolean approveMenu = Boolean.parseBoolean(req.getParameter("approveMenu")); // true if menu is approved
    String reason       = req.getParameter("rejectionReason");                   // optional rejection reason

    try {
      // ===== Process the review via AdminDAO =====
      AdminDAO.reviewRequest(requestId, approveSpot, approveMenu, reason);

      // Set success message to display in JSP
      req.setAttribute("message", "Request reviewed successfully.");
    } catch (Exception e) {
      // Handle exception and pass error message to JSP
      req.setAttribute("message", "Error: " + e.getMessage());
    }

    // Forward to admin dashboard with success or error message
    req.getRequestDispatcher("adminDashboard.jsp").forward(req, res);
  }

  // If GET request is sent (not allowed), redirect to admin dashboard
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
          throws IOException {
    res.sendRedirect("adminDashboard.jsp");
  }
}
