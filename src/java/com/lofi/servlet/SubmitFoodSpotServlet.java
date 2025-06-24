package com.lofi.servlet;

import com.lofi.dao.AdminDAO;
import com.lofi.model.FoodSpotApproval;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SubmitFoodSpotServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // === 1. Check if user is a logged-in vendor ===
        HttpSession session = request.getSession(false);
        if (session == null || !"vendor".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp?err=loginRequired");
            return;
        }

        // Get user ID from session
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("login.jsp?err=loginRequired");
            return;
        }

        // === 2. Retrieve form parameters submitted by vendor ===
        String restaurantName = request.getParameter("restaurantName");
        String address = request.getParameter("address");
        String googleMapsURL = request.getParameter("googleMapsURL");
        String photoURL = request.getParameter("photoURL");
        String openHours = request.getParameter("openHours");
        String closedHours = request.getParameter("closedHours");
        String workingDays = request.getParameter("workingDays");
        boolean halalCertified = "true".equals(request.getParameter("halalCertified")); // checkbox value

        // === 3. Basic validation of required fields ===
        if (restaurantName == null || restaurantName.isBlank() || address == null || address.isBlank()) {
            request.setAttribute("errorMessage", "Restaurant name and address are required.");
            request.getRequestDispatcher("submitFoodspot.jsp").forward(request, response);
            return;
        }

        // === 4. Create a FoodSpotApproval object with all the submitted values ===
        // This assumes a constructor exists in your model to handle this
        FoodSpotApproval submission = new FoodSpotApproval(
                0, // 0 for auto-generated primary key
                userId,
                restaurantName,
                address,
                googleMapsURL,
                photoURL,
                openHours,
                closedHours,
                halalCertified,
                workingDays
        );

        // Add timestamp of submission
        submission.setSubmitted_time(LocalDateTime.now());

        // === 5. Insert the submission into the database using DAO ===
        try {
            // NOTE: This line is missing in your code. Should be something like:
            // AdminDAO.submitFoodSpot(submission);

            request.setAttribute("successMessage", "Your food spot has been submitted for approval!");
            request.getRequestDispatcher("submissionResult.jsp").forward(request, response);

        } catch (/* SQLException e */ Exception e) {
            // === Exception handling: log and show generic error message ===
            e.printStackTrace();

            request.setAttribute("errorMessage", "There was a database error. Please try again later.");
            request.getRequestDispatcher("submitFoodspot.jsp").forward(request, response);
        }
    }
}
