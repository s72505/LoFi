package com.lofi.dao;

import com.lofi.model.FoodSpotApproval;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class VendorDAO {

    /**  All submissions (old + new) by ONE vendor  */
    public static List<FoodSpotApproval> listSubmissions(int userId) throws SQLException {

        String sql = """
            SELECT request_id, restaurant_name, submitted_time,
                   status, rejection_reason
              FROM food_spot_approval
             WHERE user_id = ?
             ORDER BY submitted_time DESC
        """;

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet r = ps.executeQuery()) {
                List<FoodSpotApproval> list = new ArrayList<>();
                while (r.next()) {
                    FoodSpotApproval f = new FoodSpotApproval(
                            r.getInt("request_id"),
                            userId,
                            r.getString("restaurant_name"),
                            null, null, null, null, null, null, null   // not needed in the table
                    );
                    f.setSubmitted_time(r.getTimestamp("submitted_time").toLocalDateTime());
                    f.setStatus(r.getString("status"));
                    f.setRejection_reason(r.getString("rejection_reason"));
                    list.add(f);
                }
                return list;
            }
        }
    }

    private VendorDAO() { }   // utility class
}
