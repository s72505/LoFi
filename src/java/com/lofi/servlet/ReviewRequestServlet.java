package com.lofi.servlet;

import com.lofi.dao.AdminDAO;
import com.lofi.model.FoodSpotApproval;
import com.lofi.model.MenuApproval;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReviewRequestServlet extends HttpServlet {

    @Override protected void doGet (HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { process(req, res); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { process(req, res); }

    private void process(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        /* ---------- simple admin-check ---------- */
        HttpSession session = req.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            res.sendRedirect("login.jsp?err=loginRequired");
            return;
        }

        /* ---------- id ---------- */
        int requestId;
        try { requestId = Integer.parseInt(req.getParameter("requestID")); }
        catch (Exception e) {
            res.sendRedirect("adminDashboard.jsp?err=badId");
            return;
        }

        /* ---------- load from DAO ---------- */
        try {
            FoodSpotApproval hdr   = AdminDAO.findFoodSpotApproval(requestId);
            if (hdr == null) { res.sendRedirect("adminDashboard.jsp"); return; }
            List<MenuApproval> menus = AdminDAO.listMenuApproval(requestId);

            /* put into session using SAME names adminSummary.jsp expects */
            session.setAttribute("requestID"      , hdr.getRequest_id());
            session.setAttribute("restaurantName" , hdr.getRestaurant_name());
            session.setAttribute("address"        , hdr.getAddress());
            session.setAttribute("googleMapsURL"  , hdr.getGoogle_maps_url());
            session.setAttribute("photoURL"       , hdr.getPhoto_url());
            session.setAttribute("openHours"      , hdr.getOpen_hours());
            session.setAttribute("closedHours"    , hdr.getClosed_hours());
            session.setAttribute("halalCertified" , hdr.getHalal_flag());
            session.setAttribute("workingDays"    , hdr.getWorking_days());
            session.setAttribute("menus"          , menus);   // list< MenuApproval > – use item.image_url etc.

            res.sendRedirect("adminSummary.jsp");

        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
