package com.lofi.servlet;

import com.lofi.dao.DBHelper;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class RegisterServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String pass = req.getParameter("password");
        String role = req.getParameter("role");
        
        boolean success = false;
        
        try (Connection c = DBHelper.getConnection()) {
            
            PreparedStatement ck = c.prepareStatement(
            "SELECT 1 FROM users WHERE email=?");
            ck.setString(1, email);
            if (!ck.executeQuery().next()) {
                
                PreparedStatement ins = c.prepareStatement(
                "INSERT INTO users(name, email, phone, pw_hash, role) VALUES(?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                
                ins.setString(1, name);
                ins.setString(2, email);
                ins.setString(3, phone);
                ins.setString(4, DigestUtils.sha256Hex(pass));
                ins.setString(5, role);
                ins.executeUpdate();
                success = true;
                
                ResultSet k = ins.getGeneratedKeys();
                if (k.next()) {
                    int newId = k.getInt(1);
                    System.out.println("New user auto-ID = " + newId);
                }
            }
        } catch (SQLException e) { 
            throw new ServletException(e); 
        }
        
        if (success) { 
            res.sendRedirect("login.jsp?msg=registered");
        } else {
            req.setAttribute("err", "E-mail already exists.");
            req.getRequestDispatcher("register.jsp").forward(req, res);
        }
    }// </editor-fold>

}
