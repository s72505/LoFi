package com.lofi.dao;

import com.lofi.model.FoodSpotApproval;
import com.lofi.model.MenuApproval;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class AdminDAO {

    private AdminDAO() {}

    public static List<FoodSpotApproval> listPendingRequests() throws SQLException {
        // This method is correct and remains unchanged.
        String sql = "SELECT f.request_id, f.restaurant_name, u.user_id, u.name, u.phone, f.submitted_time FROM food_spot_approval f JOIN users u ON f.user_id = u.user_id WHERE f.status IS NULL ORDER BY f.submitted_time DESC";
        List<FoodSpotApproval> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new FoodSpotApproval(rs.getInt("request_id"), rs.getInt("user_id"), rs.getString("restaurant_name"), rs.getString("name"), rs.getString("phone"), rs.getTimestamp("submitted_time").toLocalDateTime()));
            }
        }
        return out;
    }

    public static FoodSpotApproval findFoodSpotApproval(int requestId) throws SQLException {
        // ========== CORRECTED SQL QUERY ==========
        String sql = "SELECT request_id, user_id, restaurant_name, address, Maps_url, photo_url, open_hours, closed_hours, halal_flag, working_days FROM food_spot_approval WHERE request_id=?";
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet r = ps.executeQuery()) {
                if (!r.next()) return null;
                return new FoodSpotApproval(
                        r.getInt("request_id"),
                        r.getInt("user_id"),
                        r.getString("restaurant_name"),
                        r.getString("address"),
                        r.getString("Maps_url"), // CORRECTED
                        r.getString("photo_url"),
                        r.getString("open_hours"),
                        r.getString("closed_hours"),
                        r.getObject("halal_flag") == null ? null : r.getBoolean("halal_flag"),
                        r.getString("working_days")
                );
            }
        }
    }

    public static List<MenuApproval> listMenuApproval(int requestId) throws SQLException {
        // This method is correct and remains unchanged.
        String sql = "SELECT item_id, request_id, dish_name, price, description, cuisine_type, image_url, status FROM menu_approval WHERE request_id = ? ORDER BY dish_name";
        List<MenuApproval> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    out.add(new MenuApproval(r.getInt("item_id"), r.getInt("request_id"), r.getString("dish_name"), r.getDouble("price"), r.getString("description"), r.getString("cuisine_type"), r.getString("image_url"), r.getString("status")));
                }
            }
        }
        return out;
    }

    public static void reviewRequest(int requestId, boolean approveSpot, boolean approveMenu, String rejectionReason) throws SQLException {
        // This method relies on the corrected findFoodSpotApproval method above, so it is now fixed.
        String now = LocalDateTime.now().toString();
        try (Connection c = DBHelper.getConnection()) {
            c.setAutoCommit(false);
            int newSpotId = 0;
            if (approveSpot) {
                FoodSpotApproval hdr = findFoodSpotApproval(requestId);
                if (hdr == null) throw new SQLException("No such request " + requestId);
                // ========== CORRECTED SQL QUERY ==========
                try (PreparedStatement ins = c.prepareStatement("INSERT INTO food_spots (user_id, restaurant_name, address, Maps_url, photo_url, halal_flag, open_hours, closed_hours, working_days, rating) VALUES (?,?,?,?,?,?,?,?,?,0.0)", Statement.RETURN_GENERATED_KEYS)) {
                    ins.setInt(1, hdr.getUser_id());
                    ins.setString(2, hdr.getRestaurant_name());
                    ins.setString(3, hdr.getAddress());
                    ins.setString(4, hdr.getMaps_url()); // CORRECTED
                    ins.setString(5, hdr.getPhoto_url());
                    if (hdr.getHalal_flag() == null) ins.setNull(6, Types.BOOLEAN);
                    else ins.setBoolean(6, hdr.getHalal_flag());
                    ins.setString(7, hdr.getOpen_hours());
                    ins.setString(8, hdr.getClosed_hours());
                    ins.setString(9, hdr.getWorking_days());
                    ins.executeUpdate();
                    try (ResultSet gk = ins.getGeneratedKeys()) { if (gk.next()) newSpotId = gk.getInt(1); }
                }
                // ... (rest of approval logic)
            }
            // ... (rest of method)
            c.commit();
        }
    }
    
    // ========== THIS IS THE NEWLY ADDED/CORRECTED METHOD FOR SUBMISSIONS ==========
    public static void createSubmission(FoodSpotApproval spot, List<MenuApproval> menus) throws SQLException {
        String spotSQL = "INSERT INTO food_spot_approval (user_id, restaurant_name, address, Maps_url, photo_url, open_hours, closed_hours, halal_flag, working_days, submitted_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String menuSQL = "INSERT INTO menu_approval (request_id, dish_name, price, description, cuisine_type, image_url, submitted_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = DBHelper.getConnection();
            con.setAutoCommit(false);
            PreparedStatement psSpot = con.prepareStatement(spotSQL, Statement.RETURN_GENERATED_KEYS);
            psSpot.setInt(1, spot.getUser_id());
            psSpot.setString(2, spot.getRestaurant_name());
            psSpot.setString(3, spot.getAddress());
            psSpot.setString(4, spot.getMaps_url()); // CORRECTED
            psSpot.setString(5, spot.getPhoto_url());
            psSpot.setString(6, spot.getOpen_hours());
            psSpot.setString(7, spot.getClosed_hours());
            psSpot.setBoolean(8, spot.getHalal_flag());
            psSpot.setString(9, spot.getWorking_days());
            psSpot.setTimestamp(10, java.sql.Timestamp.valueOf(spot.getSubmitted_time()));
            psSpot.executeUpdate();
            ResultSet generatedKeys = psSpot.getGeneratedKeys();
            int newRequestId = 0;
            if (generatedKeys.next()) { newRequestId = generatedKeys.getInt(1); }
            else { throw new SQLException("Creating food spot approval failed, no ID obtained."); }
            PreparedStatement psMenu = con.prepareStatement(menuSQL);
            for (MenuApproval item : menus) {
                psMenu.setInt(1, newRequestId);
                psMenu.setString(2, item.getDish_name());
                psMenu.setDouble(3, item.getPrice());
                psMenu.setString(4, item.getDescription());
                psMenu.setString(5, item.getCuisine_type());
                psMenu.setString(6, item.getImage_url());
                psMenu.setTimestamp(7, java.sql.Timestamp.valueOf(spot.getSubmitted_time()));
                psMenu.addBatch();
            }
            psMenu.executeBatch();
            con.commit();
        } catch (SQLException e) {
            if (con != null) { try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            throw e;
        } finally {
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}