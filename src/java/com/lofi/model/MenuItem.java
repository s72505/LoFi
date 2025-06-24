package com.lofi.model;

import java.io.Serializable;

/**
 * Represents a single approved menu item associated with a specific food spot.
 */
public class MenuItem implements Serializable {

    // Unique identifier for the menu item (primary key)
    private int item_id;

    // ID of the food spot this menu item belongs to (foreign key)
    private int spot_id;

    // Name of the dish (e.g., "Nasi Lemak")
    private String dish_name;

    // Price of the dish
    private double price;

    // Description of the dish (optional detail)
    private String description;

    // Cuisine type/category (e.g., Malay, Indian, Western)
    private String cuisine_type;

    // URL pointing to an image representing the dish
    private String image_url;

    // ===== Default Constructor =====

    public MenuItem() {
        // Required for frameworks like JSP/Servlet or when used in collections
    }

    // ===== Getters and Setters for each property =====

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(int spot_id) {
        this.spot_id = spot_id;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine_type() {
        return cuisine_type;
    }

    public void setCuisine_type(String cuisine_type) {
        this.cuisine_type = cuisine_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
