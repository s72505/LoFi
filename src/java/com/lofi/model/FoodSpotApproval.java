package com.lofi.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FoodSpotApproval implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- All fields ---
    private int     request_id;
    private int     user_id;
    private String  restaurant_name;
    private String  address;
    private String  Maps_url; // CORRECTED
    private String  photo_url;
    private String  open_hours;
    private String  closed_hours;
    private Boolean halal_flag;
    private String  working_days;
    private LocalDateTime submitted_time;
    private String  status;
    private String  rejection_reason;
    private String  name;
    private String  phone;

    // --- Constructors ---
    public FoodSpotApproval() {}

    public FoodSpotApproval(int request_id, int user_id, String restaurant_name,
                            String name, String phone, LocalDateTime submitted_time) {
        this.request_id = request_id;
        this.user_id = user_id;
        this.restaurant_name = restaurant_name;
        this.name = name;
        this.phone = phone;
        this.submitted_time = submitted_time;
    }

    public FoodSpotApproval(int request_id, int user_id, String restaurant_name,
                            String address, String Maps_url, String photo_url, // CORRECTED
                            String open_hours, String closed_hours,
                            Boolean halal_flag, String working_days) {
        this.request_id = request_id;
        this.user_id = user_id;
        this.restaurant_name = restaurant_name;
        this.address = address;
        this.Maps_url = Maps_url; // CORRECTED
        this.photo_url = photo_url;
        this.open_hours = open_hours;
        this.closed_hours = closed_hours;
        this.halal_flag = halal_flag;
        this.working_days = working_days;
    }

    // --- Getters and Setters ---
    public int getRequest_id() { return request_id; }
    public void setRequest_id(int v) { request_id = v; }
    public int getUser_id() { return user_id; }
    public void setUser_id(int v) { user_id = v; }
    public String getRestaurant_name() { return restaurant_name; }
    public void setRestaurant_name(String v) { restaurant_name = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { address = v; }

    // ========== CORRECTED GETTER AND SETTER ==========
    public String getMaps_url() { return Maps_url; }
    public void setMaps_url(String v) { this.Maps_url = v; }

    public String getPhoto_url() { return photo_url; }
    public void setPhoto_url(String v) { photo_url = v; }
    public String getOpen_hours() { return open_hours; }
    public void setOpen_hours(String v) { open_hours = v; }
    public String getClosed_hours() { return closed_hours; }
    public void setClosed_hours(String v) { closed_hours = v; }
    public Boolean getHalal_flag() { return halal_flag; }
    public void setHalal_flag(Boolean v) { halal_flag = v; }
    public String getWorking_days() { return working_days; }
    public void setWorking_days(String v) { working_days = v; }
    public LocalDateTime getSubmitted_time() { return submitted_time; }
    public void setSubmitted_time(LocalDateTime v) { submitted_time = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public String getRejection_reason() { return rejection_reason; }
    public void setRejection_reason(String v) { rejection_reason = v; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; }
    
    // --- Convenience Getters ---
    public int getRequestID() { return request_id; }
    public String getRestaurantName() { return restaurant_name; }
    public LocalDateTime getSubmittedTime() { return submitted_time; }
    public String getRejectionReason() { return rejection_reason; }
}