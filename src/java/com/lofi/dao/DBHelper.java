package com.lofi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBHelper {
    
    // JDBC URL, username, and password to connect to the MySQL database
    private static final String URL = "jdbc:mysql://localhost:3306/lofi";
    private static final String USER = "root";
    private static final String PASS = "admin";
    
    // Static block to load the MySQL JDBC driver when the class is first loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL connector
        } catch (ClassNotFoundException e) {
            // If the driver is not found, initialization fails
            throw new ExceptionInInitializerError(e);
        }
    }
    
    // Public method to get a new database connection using the specified URL, user, and password
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    // Private constructor to prevent instantiation of utility class
    private DBHelper() {
    }
}
