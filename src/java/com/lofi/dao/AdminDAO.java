package com.lofi.dao;

import com.lofi.model.FoodSpotApproval;
import com.lofi.model.MenuApproval;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class AdminDAO {

    // Private constructor to prevent instantiation
    private AdminDAO() {}

    // List all pending food spot submissions for admin review
    public static List<FoodSpotApproval> listPendingRequests() throws SQLException {
        String sql = "SELECT f.request_id, f.restaurant_name, u.user_id, u.name, u.phone, f.submitted_time FROM food_spot_approval f JOIN users u ON f.user_id = u.user_id WHERE f.status IS NULL ORDER BY f.submitted_time DESC";
        List<FoodSpotApproval> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new FoodSpotApproval(
                        rs.getInt("request_id"),
                        rs.getInt("user_id"),
                        rs.getString("restaurant_name"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getTimestamp("submitted_time").toLocalDateTime()));
            }
        }
        return out;
    }

    // Retrieve full details of a specific food spot approval request
    public static FoodSpotApproval findFoodSpotApproval(int requestId) throws SQLException {
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
                        r.getString("Maps_url"),
                        r.getString("photo_url"),
                        r.getString("open_hours"),
                        r.getString("closed_hours"),
                        r.getObject("halal_flag") == null ? null : r.getBoolean("halal_flag"),
                        r.getString("working_days"));
            }
        }
    }

    // Retrieve all menu items associated with a specific submission request
    public static List<MenuApproval> listMenuApproval(int requestId) throws SQLException {
        String sql = "SELECT item_id, request_id, dish_name, price, description, cuisine_type, image_url, status FROM menu_approval WHERE request_id = ? ORDER BY dish_name";
        List<MenuApproval> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    out.add(new MenuApproval(
                            r.getInt("item_id"),
                            r.getInt("request_id"),
                            r.getString("dish_name"),
                            r.getDouble("price"),
                            r.getString("description"),
                            r.getString("cuisine_type"),
                            r.getString("image_url"),
                            r.getString("status")));
                }
            }
        }
        return out;
    }

    // Review and process a request — approve/reject both spot and menus
    public static void reviewRequest(int requestId, boolean approveSpot, boolean approveMenu, String rejectionReason) throws SQLException {
        String now = LocalDateTime.now().toString();
        try (Connection c = DBHelper.getConnection()) {
            c.setAutoCommit(false);
            int newSpotId = 0;

            // If spot is approved → insert into main food_spots table
            if (approveSpot) {
                FoodSpotApproval hdr = findFoodSpotApproval(requestId);
                if (hdr == null) throw new SQLException("No such request " + requestId);
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO food_spots (user_id, restaurant_name, address, Maps_url, photo_url, halal_flag, open_hours, closed_hours, working_days, rating) VALUES (?,?,?,?,?,?,?,?,?,0.0)", 
                        Statement.RETURN_GENERATED_KEYS)) {
                    ins.setInt(1, hdr.getUser_id());
                    ins.setString(2, hdr.getRestaurant_name());
                    ins.setString(3, hdr.getAddress());
                    ins.setString(4, hdr.getMaps_url());
                    ins.setString(5, hdr.getPhoto_url());
                    if (hdr.getHalal_flag() == null) ins.setNull(6, Types.BOOLEAN);
                    else ins.setBoolean(6, hdr.getHalal_flag());
                    ins.setString(7, hdr.getOpen_hours());
                    ins.setString(8, hdr.getClosed_hours());
                    ins.setString(9, hdr.getWorking_days());
                    ins.executeUpdate();
                    try (ResultSet gk = ins.getGeneratedKeys()) {
                        if (gk.next()) newSpotId = gk.getInt(1);
                    }
                }

                // Mark the spot as approved in approval table
                try (PreparedStatement upd = c.prepareStatement(
                        "UPDATE food_spot_approval SET status='approved', reviewed_time=?, rejection_reason=NULL WHERE request_id=?")) {
                    upd.setString(1, now);
                    upd.setInt(2, requestId);
                    upd.executeUpdate();
                }

            } else {
                // Spot rejected → record reason
                try (PreparedStatement upd = c.prepareStatement(
                        "UPDATE food_spot_approval SET status='rejected', reviewed_time=?, rejection_reason=? WHERE request_id=?")) {
                    upd.setString(1, now);
                    upd.setString(2, rejectionReason);
                    upd.setInt(3, requestId);
                    upd.executeUpdate();
                }
            }

            // If spot and menus approved → insert menu items into main table
            if (approveSpot && approveMenu && newSpotId > 0) {
                for (MenuApproval m : listMenuApproval(requestId)) {
                    try (PreparedStatement insM = c.prepareStatement(
                            "INSERT INTO menu_items (spot_id, dish_name, price, description, cuisine_type, image_url) VALUES (?,?,?,?,?,?)")) {
                        insM.setInt(1, newSpotId);
                        insM.setString(2, m.getDish_name());
                        insM.setDouble(3, m.getPrice());
                        insM.setString(4, m.getDescription());
                        insM.setString(5, m.getCuisine_type());
                        insM.setString(6, m.getImage_url());
                        insM.executeUpdate();
                    }
                }

                // Update menu approval status to approved
                try (PreparedStatement updM = c.prepareStatement(
                        "UPDATE menu_approval SET status='approved', reviewed_time=?, rejection_reason=NULL WHERE request_id=?")) {
                    updM.setString(1, now);
                    updM.setInt(2, requestId);
                    updM.executeUpdate();
                }

            } else {
                // Menu rejected → update status and log reason
                try (PreparedStatement updM = c.prepareStatement(
                        "UPDATE menu_approval SET status='rejected', reviewed_time=?, rejection_reason=? WHERE request_id=?")) {
                    updM.setString(1, now);
                    updM.setString(2, rejectionReason);
                    updM.setInt(3, requestId);
                    updM.executeUpdate();
                }
            }

            // Commit the entire transaction
            c.commit();
        }
    }

    // Submit a new food spot + associated menus for admin review
    public static void createSubmission(FoodSpotApproval spot, List<MenuApproval> menus) throws SQLException {
        String spotSQL = "INSERT INTO food_spot_approval (user_id, restaurant_name, address, Maps_url, photo_url, open_hours, closed_hours, halal_flag, working_days, submitted_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String menuSQL = "INSERT INTO menu_approval (request_id, dish_name, price, description, cuisine_type, image_url, submitted_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = DBHelper.getConnection();
            con.setAutoCommit(false);

            // Insert food spot submission
            PreparedStatement psSpot = con.prepareStatement(spotSQL, Statement.RETURN_GENERATED_KEYS);
            psSpot.setInt(1, spot.getUser_id());
            psSpot.setString(2, spot.getRestaurant_name());
            psSpot.setString(3, spot.getAddress());
            psSpot.setString(4, spot.getMaps_url());
            psSpot.setString(5, spot.getPhoto_url());
            psSpot.setString(6, spot.getOpen_hours());
            psSpot.setString(7, spot.getClosed_hours());
            psSpot.setBoolean(8, spot.getHalal_flag());
            psSpot.setString(9, spot.getWorking_days());
            psSpot.setTimestamp(10, Timestamp.valueOf(spot.getSubmitted_time()));
            psSpot.executeUpdate();

            // Get generated request ID for linking menu items
            ResultSet generatedKeys = psSpot.getGeneratedKeys();
            int newRequestId = 0;
            if (generatedKeys.next()) {
                newRequestId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating food spot approval failed, no ID obtained.");
            }

            // Batch insert menu items tied to the food spot
            PreparedStatement psMenu = con.prepareStatement(menuSQL);
            for (MenuApproval item : menus) {
                psMenu.setInt(1, newRequestId);
                psMenu.setString(2, item.getDish_name());
                psMenu.setDouble(3, item.getPrice());
                psMenu.setString(4, item.getDescription());
                psMenu.setString(5, item.getCuisine_type());
                psMenu.setString(6, item.getImage_url());
                psMenu.setTimestamp(7, Timestamp.valueOf(spot.getSubmitted_time()));
                psMenu.addBatch();
            }

            psMenu.executeBatch(); // execute all inserts
            con.commit(); // commit transaction

        } catch (SQLException e) {
            // Rollback if error occurs
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e;
        } finally {
            // Always close connection
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
