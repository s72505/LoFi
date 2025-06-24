package com.lofi.model;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private int item_id;
    private int spot_id;
    private String dish_name;
    private double price;
    private String description;
    private String cuisine_type;
    private String image_url;

    public MenuItem() {
    }

    // Getters and Setters for all fields
    public int getItem_id() { return item_id; }
    public void setItem_id(int item_id) { this.item_id = item_id; }
    public int getSpot_id() { return spot_id; }
    public void setSpot_id(int spot_id) { this.spot_id = spot_id; }
    public String getDish_name() { return dish_name; }
    public void setDish_name(String dish_name) { this.dish_name = dish_name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCuisine_type() { return cuisine_type; }
    public void setCuisine_type(String cuisine_type) { this.cuisine_type = cuisine_type; }
    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }
}