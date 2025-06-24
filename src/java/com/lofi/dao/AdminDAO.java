package com.lofi.dao;

import com.lofi.model.FoodSpotApproval;
import com.lofi.model.MenuApproval;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class AdminDAO {

    private AdminDAO() {}

    /* ------------------------------------------------------------
       1.  PENDING LIST
       ------------------------------------------------------------ */
    public static List<FoodSpotApproval> listPendingRequests() throws SQLException {

        String sql = """
            SELECT f.request_id, f.restaurant_name, u.user_id, u.name, u.phone, f.submitted_time
              FROM food_spot_approval f
              JOIN users u ON f.user_id = u.user_id
             WHERE f.status IS NULL
             ORDER BY f.submitted_time DESC
            """;

        List<FoodSpotApproval> out = new ArrayList<>();

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new FoodSpotApproval(
                        rs.getInt   ("request_id"),
                        rs.getInt   ("user_id"),
                        rs.getString("restaurant_name"),
                        rs.getString("name"),          // vendor’s name
                        rs.getString("phone"),         // phone
                        rs.getTimestamp("submitted_time").toLocalDateTime()
                ));
            }
        }
        return out;
    }

    /* ------------------------------------------------------------
       2.  SINGLE HEADER
       ------------------------------------------------------------ */
    public static FoodSpotApproval findFoodSpotApproval(int requestId) throws SQLException {

        String sql = """
            SELECT request_id, user_id, restaurant_name, address, google_maps_url,
                   photo_url, open_hours, closed_hours, halal_flag, working_days
              FROM food_spot_approval
             WHERE request_id=? 
            """;

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            try (ResultSet r = ps.executeQuery()) {
                if (!r.next()) return null;

                return new FoodSpotApproval(
                        r.getInt   ("request_id"),
                        r.getInt   ("user_id"),
                        r.getString("restaurant_name"),
                        r.getString("address"),
                        r.getString("google_maps_url"),
                        r.getString("photo_url"),
                        r.getString("open_hours"),
                        r.getString("closed_hours"),
                        r.getObject("halal_flag") == null ? null : r.getBoolean("halal_flag"),
                        r.getString("working_days")
                );
            }
        }
    }

    /* ------------------------------------------------------------
       3.  MENU LIST
       ------------------------------------------------------------ */
    public static List<MenuApproval> listMenuApproval(int requestId) throws SQLException {

        String sql = """
            SELECT item_id, request_id, dish_name, price, description, cuisine_type, image_url, status
              FROM menu_approval
             WHERE request_id = ?
             ORDER BY dish_name
            """;

        List<MenuApproval> out = new ArrayList<>();

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    out.add(new MenuApproval(
                            r.getInt   ("item_id"),
                            r.getInt   ("request_id"),
                            r.getString("dish_name"),
                            r.getDouble("price"),
                            r.getString("description"),
                            r.getString("cuisine_type"),
                            r.getString("image_url"),
                            r.getString("status")
                    ));
                }
            }
        }
        return out;
    }

    /* ------------------------------------------------------------
       4.  APPROVE / REJECT
       ------------------------------------------------------------ */
    public static void reviewRequest(int requestId,
                                     boolean approveSpot,
                                     boolean approveMenu,
                                     String rejectionReason) throws SQLException {

        String now = LocalDateTime.now().toString();

        try (Connection c = DBHelper.getConnection()) {
            c.setAutoCommit(false);

            int newSpotId = 0;

            /* ---------- header ---------- */
            if (approveSpot) {

                FoodSpotApproval hdr = findFoodSpotApproval(requestId);
                if (hdr == null) throw new SQLException("No such request " + requestId);

                /* insert live spot */
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO food_spots " +
                        "(user_id, restaurant_name, address, google_maps_url, photo_url, " +
                        " halal_flag, open_hours, closed_hours, working_days, rating) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,0.0)",
                        Statement.RETURN_GENERATED_KEYS)) {

                    ins.setInt   (1, hdr.getUser_id());
                    ins.setString(2, hdr.getRestaurant_name());
                    ins.setString(3, hdr.getAddress());
                    ins.setString(4, hdr.getGoogle_maps_url());
                    ins.setString(5, hdr.getPhoto_url());

                    if (hdr.getHalal_flag() == null) ins.setNull(6, Types.BOOLEAN);
                    else                             ins.setBoolean(6, hdr.getHalal_flag());

                    ins.setString(7, hdr.getOpen_hours());
                    ins.setString(8, hdr.getClosed_hours());
                    ins.setString(9, hdr.getWorking_days());
                    ins.executeUpdate();

                    try (ResultSet gk = ins.getGeneratedKeys()) {
                        if (gk.next()) newSpotId = gk.getInt(1);
                    }
                }

                try (PreparedStatement upd = c.prepareStatement(
                        "UPDATE food_spot_approval SET status='approved', reviewed_time=?, rejection_reason=NULL WHERE request_id=?")) {
                    upd.setString(1, now);
                    upd.setInt(2, requestId);
                    upd.executeUpdate();
                }

            } else {
                try (PreparedStatement upd = c.prepareStatement(
                        "UPDATE food_spot_approval SET status='rejected', reviewed_time=?, rejection_reason=? WHERE request_id=?")) {
                    upd.setString(1, now);
                    upd.setString(2, rejectionReason);
                    upd.setInt(3, requestId);
                    upd.executeUpdate();
                }
            }

            /* ---------- menu ---------- */
            if (approveSpot && approveMenu && newSpotId > 0) {

                for (MenuApproval m : listMenuApproval(requestId)) {
                    try (PreparedStatement insM = c.prepareStatement(
                            "INSERT INTO menu_items " +
                            "(spot_id, dish_name, price, description, cuisine_type, image_url) " +
                            "VALUES (?,?,?,?,?,?)")) {

                        insM.setInt   (1, newSpotId);
                        insM.setString(2, m.getDish_name());
                        insM.setDouble(3, m.getPrice());
                        insM.setString(4, m.getDescription());
                        insM.setString(5, m.getCuisine_type());
                        insM.setString(6, m.getImage_url());
                        insM.executeUpdate();
                    }
                }

                try (PreparedStatement updM = c.prepareStatement(
                        "UPDATE menu_approval SET status='approved', reviewed_time=?, rejection_reason=NULL WHERE request_id=?")) {
                    updM.setString(1, now);
                    updM.setInt(2, requestId);
                    updM.executeUpdate();
                }

            } else {
                try (PreparedStatement updM = c.prepareStatement(
                        "UPDATE menu_approval SET status='rejected', reviewed_time=?, rejection_reason=? WHERE request_id=?")) {
                    updM.setString(1, now);
                    updM.setString(2, rejectionReason);
                    updM.setInt(3, requestId);
                    updM.executeUpdate();
                }
            }

            c.commit();
        }
    }
    
    // In AdminDAO.java or a new DAO file
    public static void createFoodSpotSubmission(FoodSpotApproval submission) throws SQLException {
        String sql = "INSERT INTO food_spot_approval (user_id, restaurant_name, address, Maps_url, photo_url, open_hours, closed_hours, halal_flag, working_days, submitted_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, submission.getUser_id());
            ps.setString(2, submission.getRestaurant_name());
            ps.setString(3, submission.getAddress());
            ps.setString(4, submission.getGoogle_maps_url());
            ps.setString(5, submission.getPhoto_url());
            ps.setString(6, submission.getOpen_hours());
            ps.setString(7, submission.getClosed_hours());
            ps.setBoolean(8, submission.getHalal_flag());
            ps.setString(9, submission.getWorking_days());
            ps.setTimestamp(10, java.sql.Timestamp.valueOf(submission.getSubmitted_time()));

            ps.executeUpdate();
        }
    }

}
