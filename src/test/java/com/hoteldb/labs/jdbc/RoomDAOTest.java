package com.hoteldb.labs.jdbc;

import com.hoteldb.labs.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoomDAO (Lab 3)
 */
class RoomDAOTest {
    private RoomDAO roomDAO;
    private DatabaseConnection dbConnection;

    @BeforeEach
    void setUp() throws SQLException {
        dbConnection = DatabaseConnection.getInstance();
        roomDAO = new RoomDAO();
        
        // Initialize test database
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS clients");
            stmt.execute("DROP TABLE IF EXISTS rooms");
            stmt.execute("CREATE TABLE rooms (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "room_number VARCHAR(10) NOT NULL UNIQUE, " +
                    "room_type VARCHAR(50) NOT NULL, " +
                    "price_per_night DECIMAL(10, 2) NOT NULL, " +
                    "is_available BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }

    @Test
    void testCreate() throws SQLException {
        Room room = new Room("101", "Standard", new BigDecimal("50.00"), true);
        Room created = roomDAO.create(room);
        
        assertNotNull(created.getId(), "Room should have an ID after creation");
        assertEquals("101", created.getRoomNumber());
        assertEquals("Standard", created.getRoomType());
        assertEquals(new BigDecimal("50.00"), created.getPricePerNight());
        assertTrue(created.getIsAvailable());
    }

    @Test
    void testFindById() throws SQLException {
        Room room = new Room("102", "Deluxe", new BigDecimal("100.00"), true);
        room = roomDAO.create(room);
        
        Room found = roomDAO.findById(room.getId());
        assertNotNull(found, "Room should be found");
        assertEquals(room.getId(), found.getId());
        assertEquals("102", found.getRoomNumber());
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        Room found = roomDAO.findById(999);
        assertNull(found, "Room should not be found");
    }

    @Test
    void testFindAll() throws SQLException {
        roomDAO.create(new Room("201", "Standard", new BigDecimal("50.00"), true));
        roomDAO.create(new Room("202", "Deluxe", new BigDecimal("100.00"), false));
        
        List<Room> rooms = roomDAO.findAll();
        assertNotNull(rooms);
        assertTrue(rooms.size() >= 2, "Should find at least 2 rooms");
    }

    @Test
    void testUpdate() throws SQLException {
        Room room = new Room("301", "Standard", new BigDecimal("50.00"), true);
        room = roomDAO.create(room);
        
        room.setRoomType("Deluxe");
        room.setPricePerNight(new BigDecimal("100.00"));
        room.setIsAvailable(false);
        
        Room updated = roomDAO.update(room);
        assertEquals("Deluxe", updated.getRoomType());
        assertEquals(new BigDecimal("100.00"), updated.getPricePerNight());
        assertFalse(updated.getIsAvailable());
    }

    @Test
    void testDelete() throws SQLException {
        Room room = new Room("401", "Standard", new BigDecimal("50.00"), true);
        room = roomDAO.create(room);
        
        boolean deleted = roomDAO.delete(room.getId());
        assertTrue(deleted, "Room should be deleted");
        
        Room found = roomDAO.findById(room.getId());
        assertNull(found, "Deleted room should not be found");
    }

    @Test
    void testDeleteNotFound() throws SQLException {
        boolean deleted = roomDAO.delete(999);
        assertFalse(deleted, "Should return false when room not found");
    }
}

