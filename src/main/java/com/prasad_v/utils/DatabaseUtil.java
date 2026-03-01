package com.prasad_v.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {

    private Connection connection;

    public void connect(String url, String username, String password) throws SQLException {
        try {
            connection = DriverManager.getConnection(url, username, password);
            LoggerUtil.info("Database connected successfully");
        } catch (SQLException e) {
            LoggerUtil.error("Database connection failed", e);
            throw e;
        }
    }

    public List<Map<String, Object>> executeQuery(String query) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
            
            LoggerUtil.info("Query executed: " + query + " | Rows returned: " + results.size());
        }
        
        return results;
    }

    public int executeUpdate(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            LoggerUtil.info("Update executed: " + query + " | Rows affected: " + rowsAffected);
            return rowsAffected;
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LoggerUtil.info("Database disconnected");
            }
        } catch (SQLException e) {
            LoggerUtil.error("Error closing database connection", e);
        }
    }
}
