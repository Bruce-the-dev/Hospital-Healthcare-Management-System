package com.hospital.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static final Logger LOGGER =
            Logger.getLogger(DBConnection.class.getName());

    // Database URL, username, password
    private static final String URL = "jdbc:postgresql://localhost:5432/hospital_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "bruce";

    // Private constructor to prevent instantiation
    private DBConnection() {}

    // Get a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Optional: Close connection utility
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE,"Failed to close database connection", e);
            }
        }
    }
}

