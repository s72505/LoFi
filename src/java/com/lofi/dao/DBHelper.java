
package com.lofi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/lofi";
    private static final String USER = "root";
    private static final String PASS = "admin";
    
    // Load driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    private DBHelper() {
    }
}
