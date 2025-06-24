package com.lofi.dao;

import com.lofi.model.FoodSpot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class FavouriteDAO {

    public static void add(int userId, int spotId) throws SQLException {
        String sql = "INSERT IGNORE INTO favourites(user_id, spot_id) VALUES (?, ?)";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, spotId);
            ps.executeUpdate();
        }
    }

    public static void remove(int userId, int spotId) throws SQLException {
        String sql = "DELETE FROM favourites WHERE user_id = ? AND spot_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, spotId);
            ps.executeUpdate();
        }
    }

    public static boolean exists(int userId, int spotId) throws SQLException {
        String sql = "SELECT 1 FROM favourites WHERE user_id = ? AND spot_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, spotId);
            return ps.executeQuery().next();
        }
    }

    public static List<FoodSpot> listByUser(int userId) throws SQLException {
        String sql = """
            SELECT fs.*
            FROM food_spots fs
            JOIN favourites f ON fs.spot_id = f.spot_id
            WHERE f.user_id = ?
        """;
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<FoodSpot> list = new ArrayList<>();
            while (rs.next()) {
                FoodSpot f = new FoodSpot();
                f.setSpotId(rs.getInt("spot_id"));
                f.setRestaurantName(rs.getString("restaurant_name"));
                f.setAddress(rs.getString("address"));
                f.setGoogleMapsUrl(rs.getString("Maps_url"));
                f.setHalalFlag(rs.getBoolean("halal_flag"));
                f.setOpenHours(rs.getString("open_hours"));
                f.setClosedHours(rs.getString("closed_hours"));
                f.setWorkingDays(rs.getString("working_days"));
                f.setRating(rs.getDouble("rating"));
                f.setPhotoUrl(rs.getString("photo_url"));
                list.add(f);
            }
            return list;
        }
    }

    private FavouriteDAO() {}
}
