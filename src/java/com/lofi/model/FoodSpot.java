package com.lofi.model;

public class FoodSpot {

    // =======================
    // Fields / Attributes
    // =======================
    private int spotId;
    private String restaurantName;
    private String address;
    private String googleMapsUrl;
    private boolean halalFlag;
    private String openHours;
    private String closedHours;
    private String workingDays;
    private double rating;
    private String photoUrl;

    // =======================
    // Constructors
    // =======================

    // Default constructor
    public FoodSpot() {
    }

    // Parameterized constructor for full initialization
    public FoodSpot(int spotId, String restaurantName, String address, String googleMapsUrl, boolean halalFlag, String openHours, String closedHours, String workingDays, double rating, String photoUrl) {
        this.spotId = spotId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.googleMapsUrl = googleMapsUrl;
        this.halalFlag = halalFlag;
        this.openHours = openHours;
        this.closedHours = closedHours;
        this.workingDays = workingDays;
        this.rating = rating;
        this.photoUrl = photoUrl;
    }

    // =======================
    // Getters and Setters
    // =======================

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl;
    }

    public boolean isHalalFlag() {
        return halalFlag;
    }

    public void setHalalFlag(boolean halalFlag) {
        this.halalFlag = halalFlag;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getClosedHours() {
        return closedHours;
    }

    public void setClosedHours(String closedHours) {
        this.closedHours = closedHours;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
