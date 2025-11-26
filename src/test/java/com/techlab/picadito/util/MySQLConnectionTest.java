package com.techlab.picadito.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to test MySQL connection and create the database if it doesn't exist.
 * Run this class to verify MySQL setup before using the production profile.
 */
public class MySQLConnectionTest {
    
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "picadito_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Update this with your MySQL password
    
    public static void main(String[] args) {
        System.out.println("=== MySQL Connection Test ===");
        System.out.println("Host: " + HOST);
        System.out.println("Port: " + PORT);
        System.out.println("Database: " + DATABASE);
        System.out.println("Username: " + USERNAME);
        System.out.println();
        
        // Test connection without database (to create it)
        String urlWithoutDb = "jdbc:mysql://" + HOST + ":" + PORT + "/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL driver loaded successfully");
            
            // Connect to MySQL server (without specific database)
            System.out.println("Attempting to connect to MySQL server...");
            try (Connection conn = DriverManager.getConnection(urlWithoutDb, USERNAME, PASSWORD)) {
                System.out.println("✓ Connected to MySQL server successfully!");
                
                // Check if database exists
                System.out.println("Checking if database '" + DATABASE + "' exists...");
                boolean dbExists = databaseExists(conn, DATABASE);
                
                if (dbExists) {
                    System.out.println("✓ Database '" + DATABASE + "' already exists");
                } else {
                    System.out.println("Database '" + DATABASE + "' does not exist. Creating it...");
                    createDatabase(conn, DATABASE);
                    System.out.println("✓ Database '" + DATABASE + "' created successfully!");
                }
                
                // Test connection to the specific database
                System.out.println("Testing connection to database '" + DATABASE + "'...");
                String urlWithDb = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                try (Connection dbConn = DriverManager.getConnection(urlWithDb, USERNAME, PASSWORD)) {
                    System.out.println("✓ Successfully connected to database '" + DATABASE + "'!");
                    System.out.println();
                    System.out.println("=== All tests passed! ===");
                    System.out.println("You can now use the 'prod' profile in your application.");
                }
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error: MySQL driver not found!");
            System.err.println("Make sure mysql-connector-j is in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Error connecting to MySQL:");
            System.err.println("  " + e.getMessage());
            System.err.println();
            System.err.println("Common issues:");
            System.err.println("  - MySQL server is not running");
            System.err.println("  - Wrong username or password");
            System.err.println("  - MySQL is not accessible on " + HOST + ":" + PORT);
            System.err.println();
            System.err.println("Please update the connection details in this class and try again.");
            e.printStackTrace();
        }
    }
    
    private static boolean databaseExists(Connection conn, String dbName) throws SQLException {
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dbName);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private static void createDatabase(Connection conn, String dbName) throws SQLException {
        String sql = "CREATE DATABASE " + dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}

