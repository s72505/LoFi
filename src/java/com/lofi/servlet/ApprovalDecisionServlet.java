package com.lofi.servlet;

import com.lofi.dao.AdminDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ApprovalDecisionServlet extends HttpServlet {
  @Override protected void doPost(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {
    int requestId;
    try { requestId = Integer.parseInt(req.getParameter("requestID")); }
    catch (Exception e) { res.sendRedirect("adminDashboard.jsp?err=badId"); return; }

    boolean approveSpot = Boolean.parseBoolean(req.getParameter("approveSpot"));
    boolean approveMenu = Boolean.parseBoolean(req.getParameter("approveMenu"));
    String reason     = req.getParameter("rejectionReason");

    try {
      AdminDAO.reviewRequest(requestId, approveSpot, approveMenu, reason);
      req.setAttribute("message", "Request reviewed successfully.");
    } catch (Exception e) {
      req.setAttribute("message", "Error: "+e.getMessage());
    }
    req.getRequestDispatcher("adminDashboard.jsp").forward(req, res);
  }
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse res)
          throws IOException { res.sendRedirect("adminDashboard.jsp"); }
}
