package com.lofi.servlet;

import com.lofi.dao.AdminDAO;
import com.lofi.model.FoodSpotApproval;
import com.lofi.model.MenuApproval;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubmissionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"vendor".equals(role)) {
            response.sendRedirect("login.jsp?err=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("vendorDashboard.jsp");
            return;
        }

        switch (action) {
            case "set_spot_details":
                handleSetSpotDetails(request, session);
                response.sendRedirect("newSubmission.jsp");
                break;
            case "add_menu":
                handleAddMenu(request, session);
                response.sendRedirect("newSubmission.jsp");
                break;
            case "delete_menu":
                handleDeleteMenu(request, session);
                response.sendRedirect("newSubmission.jsp");
                break;
            case "final_submit":
                handleFinalSubmit(request, response, session, userId);
                break;
            default:
                response.sendRedirect("vendorDashboard.jsp");
        }
    }

    private void handleSetSpotDetails(HttpServletRequest request, HttpSession session) {
        FoodSpotApproval spot = new FoodSpotApproval();
        spot.setRestaurant_name(request.getParameter("restaurantName"));
        spot.setAddress(request.getParameter("address"));
        spot.setMaps_url(request.getParameter("googleMapsURL"));
        spot.setPhoto_url(request.getParameter("photoURL"));
        spot.setOpen_hours(request.getParameter("openHours"));
        spot.setClosed_hours(request.getParameter("closedHours"));
        spot.setWorking_days(request.getParameter("workingDays"));
        spot.setHalal_flag("true".equals(request.getParameter("halalCertified")));
        session.setAttribute("submissionSpot", spot);
        session.setAttribute("submissionMenus", new ArrayList<MenuApproval>());
    }
    
    private void handleAddMenu(HttpServletRequest request, HttpSession session) {
        List<MenuApproval> menus = (List<MenuApproval>) session.getAttribute("submissionMenus");
        if (menus == null) {
            menus = new ArrayList<>();
        }
        MenuApproval newItem = new MenuApproval(0, 0, request.getParameter("dishName"), Double.parseDouble(request.getParameter("price")), request.getParameter("description"), request.getParameter("cuisineType"), request.getParameter("imageURL"), null);
        newItem.setItem_id(Math.abs(UUID.randomUUID().hashCode()));
        menus.add(newItem);
        session.setAttribute("submissionMenus", menus);
    }

    private void handleDeleteMenu(HttpServletRequest request, HttpSession session) {
        List<MenuApproval> menus = (List<MenuApproval>) session.getAttribute("submissionMenus");
        if (menus != null) {
            int menuIdToDelete = Integer.parseInt(request.getParameter("menuId"));
            menus.removeIf(menu -> menu.getItem_id() == menuIdToDelete);
        }
    }

    private void handleFinalSubmit(HttpServletRequest request, HttpServletResponse response, HttpSession session, int userId) throws ServletException, IOException {
        FoodSpotApproval spot = (FoodSpotApproval) session.getAttribute("submissionSpot");
        List<MenuApproval> menus = (List<MenuApproval>) session.getAttribute("submissionMenus");

        if (spot == null || menus == null || menus.isEmpty()) {
            request.setAttribute("err", "Incomplete submission. Please add foodspot details and at least one menu item.");
            request.getRequestDispatcher("newSubmission.jsp").forward(request, response);
            return; // Added return for safety
        }

        spot.setUser_id(userId);
        spot.setSubmitted_time(LocalDateTime.now());
        
        try {
            AdminDAO.createSubmission(spot, menus);

            session.removeAttribute("submissionSpot");
            session.removeAttribute("submissionMenus");

            request.setAttribute("message", "Submission successful! Your request is now pending admin approval.");
            request.getRequestDispatcher("submissionResults.jsp").forward(request, response);
            return; // ========== DEFENSIVE RETURN ADDED ==========

        } catch (SQLException e) {
            e.printStackTrace(); 
            request.setAttribute("err", "A database error occurred: " + e.getMessage());
            request.getRequestDispatcher("newSubmission.jsp").forward(request, response);
            return; // ========== DEFENSIVE RETURN ADDED ==========
        }
    }
}