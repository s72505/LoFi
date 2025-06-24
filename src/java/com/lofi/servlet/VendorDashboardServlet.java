package com.lofi.servlet;

import com.lofi.dao.VendorDAO;
import com.lofi.model.FoodSpotApproval;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VendorDashboardServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        /* ─── guard ─── */
        HttpSession sess = req.getSession(false);
        if (sess == null || !"vendor".equals(sess.getAttribute("role"))) {
            res.sendRedirect("login.jsp?err=loginRequired");
            return;
        }
        int userId = (Integer) sess.getAttribute("userId");

        /* ─── load data ─── */
        try {
            List<FoodSpotApproval> subs = VendorDAO.listSubmissions(userId);
            req.setAttribute("submissions", subs);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

        req.getRequestDispatcher("vendorDashboard.jsp").forward(req, res);
    }

    /* POST isn’t used – keep it simple */
    @Override protected void doPost(HttpServletRequest rq,HttpServletResponse rs)
            throws IOException { rs.sendRedirect("VendorDashboardServlet"); }
}
