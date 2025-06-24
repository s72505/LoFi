//package com.lofi.dao;
//
//import com.lofi.model.FoodSpot;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public final class FoodSpotDAO {
//
//    /* ---------- MAIN SEARCH (No more cuisine filter) ---------- */
//    public static List<FoodSpot> findWithFilters(Boolean halal,
//                                                 double minPrice,
//                                                 double maxPrice) throws SQLException {
//
//        String sql = """
//            SELECT fs.*
//              FROM food_spots fs
//              JOIN menu_items mi ON fs.spot_id = mi.spot_id
//             WHERE (? IS NULL OR fs.halal_flag = ?)
//             GROUP BY fs.spot_id
//            HAVING MIN(mi.price) >= ? AND MAX(mi.price) <= ?
//            """;
//
//        try (Connection c = DBHelper.getConnection();
//             PreparedStatement ps = c.prepareStatement(sql)) {
//
//            ps.setObject(1, halal); // IS NULL
//            ps.setObject(2, halal); // fs.halal_flag = ?
//            ps.setDouble(3, minPrice);
//            ps.setDouble(4, maxPrice);
//
//            ResultSet rs = ps.executeQuery();
//            List<FoodSpot> list = new ArrayList<>();
//            while (rs.next()) list.add(map(rs));
//            return list;
//        }
//    }

package com.lofi.dao;

import com.lofi.model.FoodSpot;
//import com.lofi.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class FoodSpotDAO {

    /**
     * Find spots, filtering by:
     *   • halal flag (nullable)
     *   • price range (based on menu_items.price)
     *   • minimum rating (food_spots.rating)
     *   • sort order (name, price, rating)
     */
    public static List<FoodSpot> findWithFilters(Boolean halal,
                                                 double minPrice,
                                                 double maxPrice,
                                                 double minRating,
                                                 String sort) throws SQLException {
        // Base SQL with grouping and price‐range + rating filter
        StringBuilder sql = new StringBuilder("""
            SELECT fs.*
              FROM food_spots fs
              JOIN menu_items mi ON fs.spot_id = mi.spot_id
             WHERE (? IS NULL OR fs.halal_flag = ?)
             GROUP BY fs.spot_id
            HAVING MIN(mi.price) >= ?
               AND MAX(mi.price) <= ?
               AND fs.rating    >= ?
            """);

        // Append ORDER BY based on `sort` param
        switch (sort) {
            case "price_asc":
                sql.append(" ORDER BY MIN(mi.price) ASC");
                break;
            case "price_desc":
                sql.append(" ORDER BY MAX(mi.price) DESC");
                break;
            case "rating_desc":
                sql.append(" ORDER BY fs.rating DESC");
                break;
            default:
                sql.append(" ORDER BY fs.restaurant_name ASC");
        }

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int idx = 1;
            // halal filter (twice for the IS NULL OR = ? clause)
            ps.setObject(idx++, halal);
            ps.setObject(idx++, halal);
            // price range
            ps.setDouble(idx++, minPrice);
            ps.setDouble(idx++, maxPrice);
            // rating
            ps.setDouble(idx++, minRating);

            try (ResultSet rs = ps.executeQuery()) {
                List<FoodSpot> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        }
    }
    
    
    
    /* ---------- SINGLE RECORD ---------- */
    public static FoodSpot find(int id) throws SQLException {
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM food_spots WHERE spot_id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    /* ---------- INSERT ---------- */
    public static int insert(FoodSpot f) throws SQLException {
        String sql = """
            INSERT INTO food_spots (
                restaurant_name, address, google_maps_url,
                halal_flag, rating, photo_url,
                open_hours, closed_hours, working_days
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, f.getRestaurantName());
            ps.setString(2, f.getAddress());
            ps.setString(3, f.getGoogleMapsUrl());
            ps.setBoolean(4, f.isHalalFlag());
            ps.setDouble(5, f.getRating());
            ps.setString(6, f.getPhotoUrl());
            ps.setString(7, f.getOpenHours());
            ps.setString(8, f.getClosedHours());
            ps.setString(9, f.getWorkingDays());

            ps.executeUpdate();

            ResultSet k = ps.getGeneratedKeys();
            return k.next() ? k.getInt(1) : 0;
        }
    }

    private static FoodSpot map(ResultSet r) throws SQLException {
        FoodSpot f = new FoodSpot();
        f.setSpotId(r.getInt("spot_id"));
        f.setRestaurantName(r.getString("restaurant_name"));
        f.setAddress(r.getString("address"));
        f.setGoogleMapsUrl(r.getString("google_maps_url"));
        f.setHalalFlag(r.getBoolean("halal_flag"));
        f.setRating(r.getDouble("rating"));
        f.setPhotoUrl(r.getString("photo_url"));
        f.setOpenHours(r.getString("open_hours"));
        f.setClosedHours(r.getString("closed_hours"));
        f.setWorkingDays(r.getString("working_days"));
        return f;
    }

    // prevent instantiation
    private FoodSpotDAO() {}
}
