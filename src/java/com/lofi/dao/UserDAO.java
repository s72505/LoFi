package com.lofi.dao;

import com.lofi.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class UserDAO {

    /* ---------- CREATE ---------- */
    public static int insert(User u) throws SQLException {
        String sql = """
            INSERT INTO users(name, email, phone, pw_hash, role)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getPwHash());
            ps.setString(5, u.getRole());

            ps.executeUpdate();
            ResultSet k = ps.getGeneratedKeys();
            return k.next() ? k.getInt(1) : 0;        // new user_id
        }
    }

    /* ---------- READ ---------- */
    public static User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public static User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public static List<User> listAll() throws SQLException {
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users ORDER BY user_id");
             ResultSet rs = ps.executeQuery()) {

            List<User> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    /* ---------- UPDATE ---------- */
    public static void updateProfile(User u) throws SQLException {
        String sql = """
            UPDATE users
               SET name  = ?, phone = ?, role = ?
             WHERE user_id = ?
        """;
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getName());
            ps.setString(2, u.getPhone());
            ps.setString(3, u.getRole());
            ps.setInt   (4, u.getUserId());
            ps.executeUpdate();
        }
    }

    public static void updatePassword(int userId, String newHash) throws SQLException {
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE users SET pw_hash = ? WHERE user_id = ?")) {

            ps.setString(1, newHash);
            ps.setInt   (2, userId);
            ps.executeUpdate();
        }
    }

    /* ---------- DELETE ---------- */
    public static void delete(int userId) throws SQLException {
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE user_id = ?")) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /* ---------- UTIL ---------- */
    public static boolean emailExists(String email) throws SQLException {
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT 1 FROM users WHERE email = ?")) {

            ps.setString(1, email);
            return ps.executeQuery().next();
        }
    }

    /* map a ResultSet row → POJO (no renaming) */
    private static User map(ResultSet r) throws SQLException {
        User u = new User();
        u.setUserId (r.getInt   ("user_id"));
        u.setName   (r.getString("name"));
        u.setEmail  (r.getString("email"));
        u.setPhone  (r.getString("phone"));
        u.setPwHash (r.getString("pw_hash"));
        u.setRole   (r.getString("role"));
        return u;
    }

    private UserDAO() {}   // utility class; no instances
}
