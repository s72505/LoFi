package com.lofi.servlet;

import com.lofi.dao.DBHelper;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    /* ----------------  GET: just show the form  ---------------- */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, res);
    }

    /* ----------------  POST: authenticate  ---------------- */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String pwd   = req.getParameter("password");
        String role  = req.getParameter("role");

        boolean ok = false;

        /* we also need the phone-number column for the profile page            */
        /* ──────────────────────────────────────────────────────────────────── */
        String sql = """
                SELECT user_id, name, phone, pw_hash
                  FROM users
                 WHERE email = ? AND role = ?""";

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next() && DigestUtils.sha256Hex(pwd).equals(rs.getString("pw_hash"))) {

                ok = true;

                HttpSession session = req.getSession(true);
                session.setAttribute("userId", rs.getInt("user_id"));
                session.setAttribute("role",   role);

                /* ─── new attributes used by profile.jsp ─── */
                session.setAttribute("name",  rs.getString("name"));
                session.setAttribute("email", email);               // we already have it
                session.setAttribute("phone", rs.getString("phone"));
                /* (keep the old one in case other pages still rely on it) */
                session.setAttribute("userName", rs.getString("name"));
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (ok) {
            res.sendRedirect("Home");          // or wherever you land after login
        } else {
            req.setAttribute("err", "Invalid e-mail or password.");
            req.getRequestDispatcher("login.jsp").forward(req, res);
        }
    }

    @Override
    public String getServletInfo() {
        return "Authenticates users for LoFi";
    }
}
