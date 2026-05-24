package com.prasad_v.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

    public List<Map<String, Object>> executeQuery(String query, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            bindParams(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }

            LoggerUtil.info("Query executed. Rows returned: " + results.size());
        }

        return results;
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            bindParams(stmt, params);
            int rowsAffected = stmt.executeUpdate();
            LoggerUtil.info("Update executed. Rows affected: " + rowsAffected);
            return rowsAffected;
        }
    }

    private void bindParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
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
