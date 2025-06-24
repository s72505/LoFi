package com.lofi.model;

import java.io.Serializable;

/**
 * Represents a menu item submitted by a vendor for approval.
 * Belongs to a specific food spot approval request.
 */
public class MenuApproval implements Serializable {
    private static final long serialVersionUID = 1L;  // For Serializable versioning

    // ========== Fields matching the 'menu_approval' table ==========
    private int    item_id;        // Unique ID for this menu item (auto-generated)
    private int    request_id;     // Foreign key linking to food_spot_approval
    private String dish_name;      // Name of the dish
    private double price;          // Price of the dish
    private String description;    // Description of the dish
    private String cuisine_type;   // Cuisine category (e.g., Malay, Western, Thai)
    private String image_url;      // URL to an image of the dish
    private String status;         // Approval status (e.g., pending, approved, rejected)

    // ========== Constructor ==========

    /**
     * Full constructor for menu approval entries.
     */
    public MenuApproval(int item_id, int request_id, String dish_name,
                        double price, String description,
                        String cuisine_type, String image_url, String status) {
        this.item_id      = item_id;
        this.request_id   = request_id;
        this.dish_name    = dish_name;
        this.price        = price;
        this.description  = description;
        this.cuisine_type = cuisine_type;
        this.image_url    = image_url;
        this.status       = status;
    }

    // ========== Getters and Setters ==========

    public int getItem_id() { return item_id; }
    public void setItem_id(int v) { this.item_id = v; }

    public int getRequest_id() { return request_id; }
    public void setRequest_id(int v) { this.request_id = v; }

    public String getDish_name() { return dish_name; }
    public void setDish_name(String v) { this.dish_name = v; }

    public double getPrice() { return price; }
    public void setPrice(double v) { this.price = v; }

    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }

    public String getCuisine_type() { return cuisine_type; }
    public void setCuisine_type(String v) { this.cuisine_type = v; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String v) { this.image_url = v; }

    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}
