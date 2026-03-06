package com.prasad_v.tests.examples;

import com.prasad_v.utils.DatabaseUtil;
import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Database Testing")
@Feature("Database Operations")
public class TestDatabaseExample {

    private DatabaseUtil db;
    
    // Update these with your actual database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    @BeforeMethod
    public void setup() throws SQLException {
        LoggerUtil.info("Connecting to database");
        db = new DatabaseUtil();
        db.connect(DB_URL, DB_USER, DB_PASSWORD);
        LoggerUtil.info("Database connected successfully");
    }

    @AfterMethod
    public void teardown() throws SQLException {
        LoggerUtil.info("Disconnecting from database");
        if (db != null) {
            db.disconnect();
        }
        LoggerUtil.info("Database disconnected");
    }

    @Test(enabled = false) // Disabled by default - enable when DB is available
    @Story("Query Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify database query returns user records")
    public void testQueryUsers() throws SQLException {
        LoggerUtil.info("Starting DB test - Query Users");
        
        String query = "SELECT * FROM users WHERE status = 'active'";
        List<Map<String, Object>> results = db.executeQuery(query);
        
        LoggerUtil.info("Query returned " + results.size() + " records");
        assertThat(results).isNotEmpty();
        
        LoggerUtil.info("DB query test completed");
    }

    @Test(enabled = false) // Disabled by default - enable when DB is available
    @Story("Insert User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify database insert operation")
    public void testInsertUser() throws SQLException {
        LoggerUtil.info("Starting DB test - Insert User");
        
        String insertQuery = "INSERT INTO users (name, email, status) VALUES ('Test User', 'test@example.com', 'active')";
        int rowsAffected = db.executeUpdate(insertQuery);
        
        LoggerUtil.info("Rows affected: " + rowsAffected);
        assertThat(rowsAffected).isEqualTo(1);
        
        LoggerUtil.info("User inserted successfully");
    }

    @Test(enabled = false) // Disabled by default - enable when DB is available
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify database update operation")
    public void testUpdateUser() throws SQLException {
        LoggerUtil.info("Starting DB test - Update User");
        
        String updateQuery = "UPDATE users SET status = 'inactive' WHERE email = 'test@example.com'";
        int rowsAffected = db.executeUpdate(updateQuery);
        
        LoggerUtil.info("Rows affected: " + rowsAffected);
        assertThat(rowsAffected).isGreaterThan(0);
        
        LoggerUtil.info("User updated successfully");
    }

    @Test(enabled = false) // Disabled by default - enable when DB is available
    @Story("Delete User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify database delete operation")
    public void testDeleteUser() throws SQLException {
        LoggerUtil.info("Starting DB test - Delete User");
        
        String deleteQuery = "DELETE FROM users WHERE email = 'test@example.com'";
        int rowsAffected = db.executeUpdate(deleteQuery);
        
        LoggerUtil.info("Rows affected: " + rowsAffected);
        assertThat(rowsAffected).isGreaterThan(0);
        
        LoggerUtil.info("User deleted successfully");
    }
}
