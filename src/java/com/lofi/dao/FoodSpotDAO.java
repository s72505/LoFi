package com.lofi.dao;

import com.lofi.model.FoodSpot;
import com.lofi.model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class FoodSpotDAO {

    /**
     * Filters food spots based on multiple criteria including cuisine, halal status, price range, and rating.
     */
    public static List<FoodSpot> findWithFilters(String cuisine,
                                                 Boolean halal,
                                                 double minPrice,
                                                 double maxPrice,
                                                 double minRating,
                                                 String sort) throws SQLException {

        // Construct SQL query with optional filters for halal and cuisine
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

        // Append sorting condition based on user input
        switch (sort) {
            case "price_asc": sql.append(" ORDER BY MIN(mi.price) ASC"); break;
            case "price_desc": sql.append(" ORDER BY MAX(mi.price) DESC"); break;
            case "rating_desc": sql.append(" ORDER BY fs.rating DESC"); break;
            default: sql.append(" ORDER BY fs.restaurant_name ASC");
        }

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int idx = 1;

            // Bind halal filter
            ps.setObject(idx++, halal);
            ps.setObject(idx++, halal);

            // Bind cuisine filter
            ps.setString(idx++, cuisine);
            ps.setString(idx++, cuisine);

            // Bind price and rating constraints
            ps.setDouble(idx++, minPrice);
            ps.setDouble(idx++, maxPrice);
            ps.setDouble(idx++, minRating);

            // Execute query and build result list
            try (ResultSet rs = ps.executeQuery()) {
                List<FoodSpot> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        }
    }

    /**
     * Finds a single food spot by its unique ID.
     */
    public static FoodSpot find(int id) throws SQLException {
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM food_spots WHERE spot_id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    /**
     * Inserts a new food spot into the database and returns the generated ID.
     */
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

            // Return generated ID
            ResultSet k = ps.getGeneratedKeys();
            return k.next() ? k.getInt(1) : 0;
        }
    }

    /**
     * Retrieves all menu items for a given food spot ID.
     */
    public static List<MenuItem> listMenuItemsBySpotId(int spotId) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE spot_id = ? ORDER BY dish_name";
        List<MenuItem> menuItems = new ArrayList<>();

        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, spotId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem();
                    item.setItem_id(rs.getInt("item_id"));
                    item.setSpot_id(rs.getInt("spot_id"));
                    item.setDish_name(rs.getString("dish_name"));
                    item.setPrice(rs.getDouble("price"));
                    item.setDescription(rs.getString("description"));
                    item.setCuisine_type(rs.getString("cuisine_type"));
                    item.setImage_url(rs.getString("image_url"));
                    menuItems.add(item);
                }
            }
        }
        return menuItems;
    }

    /**
     * Updates the average rating for a specific food spot.
     */
    public static void updateRating(int spotId, double newRating) throws SQLException {
        String sql = "UPDATE food_spots SET rating = ? WHERE spot_id = ?";
        try (Connection c = DBHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDouble(1, newRating);
            ps.setInt(2, spotId);
            ps.executeUpdate();
        }
    }

    /**
     * Helper method to map a ResultSet row into a FoodSpot object.
     */
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

    // Private constructor to prevent instantiation of utility class
    private FoodSpotDAO() {}
}
