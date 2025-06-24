package com.lofi.dao;

import com.lofi.model.FoodSpot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class FoodSpotDAO {

    /**
     * Updated to filter by cuisine type.
     */
    public static List<FoodSpot> findWithFilters(String cuisine, // New parameter
                                                 Boolean halal,
                                                 double minPrice,
                                                 double maxPrice,
                                                 double minRating,
                                                 String sort) throws SQLException {
        
        // ========== SQL QUERY UPDATED TO INCLUDE CUISINE FILTER ==========
        StringBuilder sql = new StringBuilder("""
            SELECT fs.*, mi.cuisine_type
              FROM food_spots fs
              JOIN menu_items mi ON fs.spot_id = mi.spot_id
             WHERE (? IS NULL OR fs.halal_flag = ?)
               AND (? IS NULL OR mi.cuisine_type = ?)
             GROUP BY fs.spot_id
            HAVING MIN(mi.price) >= ?
               AND MAX(mi.price) <= ?
               AND fs.rating    >= ?
            """);

        switch (sort) {
            case "price_asc": sql.append(" ORDER BY MIN(mi.price) ASC"); break;
            case "price_desc": sql.append(" ORDER BY MAX(mi.price) DESC"); break;
            case "rating_desc": sql.append(" ORDER BY fs.rating DESC"); break;
            default: sql.append(" ORDER BY fs.restaurant_name ASC");
        }

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int idx = 1;
            // Halal filter
            ps.setObject(idx++, halal);
            ps.setObject(idx++, halal);
            
            // ========== NEW CUISINE PARAMETER BINDING ==========
            ps.setString(idx++, cuisine);
            ps.setString(idx++, cuisine);
            
            // Price range
            ps.setDouble(idx++, minPrice);
            ps.setDouble(idx++, maxPrice);
            
            // Rating
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
    
    // ... (The rest of your FoodSpotDAO.java file remains unchanged)
    
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
                restaurant_name, address, Maps_url,
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
        f.setGoogleMapsUrl(r.getString("Maps_url"));
        f.setHalalFlag(r.getBoolean("halal_flag"));
        f.setRating(r.getDouble("rating"));
        f.setPhotoUrl(r.getString("photo_url"));
        f.setOpenHours(r.getString("open_hours"));
        f.setClosedHours(r.getString("closed_hours"));
        f.setWorkingDays(r.getString("working_days"));
        return f;
    }

    private FoodSpotDAO() {}
}