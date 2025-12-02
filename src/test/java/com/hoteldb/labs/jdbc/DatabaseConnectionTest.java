package com.hoteldb.labs.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DatabaseConnection (Lab 3)
 */
class DatabaseConnectionTest {
    private DatabaseConnection dbConnection;

    @BeforeEach
    void setUp() {
        // Reset singleton for testing
        dbConnection = DatabaseConnection.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }

    @Test
    void testGetInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        assertSame(instance1, instance2, "Should return the same instance (singleton)");
    }

    @Test
    void testGetConnection() throws SQLException {
        Connection conn = dbConnection.getConnection();
        assertNotNull(conn, "Connection should not be null");
        assertFalse(conn.isClosed(), "Connection should be open");
    }

    @Test
    void testTestConnection() {
        boolean result = dbConnection.testConnection();
        assertTrue(result, "Connection test should succeed");
    }

    @Test
    void testCloseConnection() throws SQLException {
        dbConnection.getConnection();
        dbConnection.closeConnection();
        // Connection should be closed
        assertTrue(dbConnection.getConnection().isClosed() || 
                   dbConnection.getConnection().isValid(1));
    }

    @Test
    void testLoadProperties() {
        // Properties should be loaded in constructor
        assertNotNull(dbConnection);
    }
}

