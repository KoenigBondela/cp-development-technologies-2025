package com.hoteldb.labs.jdbc;

import com.hoteldb.labs.model.Client;
import com.hoteldb.labs.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ClientDAO (Lab 3)
 */
class ClientDAOTest {
    private ClientDAO clientDAO;
    private RoomDAO roomDAO;
    private DatabaseConnection dbConnection;

    @BeforeEach
    void setUp() throws SQLException {
        dbConnection = DatabaseConnection.getInstance();
        clientDAO = new ClientDAO();
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
            stmt.execute("CREATE TABLE clients (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(100) NOT NULL, " +
                    "last_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(255) UNIQUE, " +
                    "phone VARCHAR(20), " +
                    "room_id INT, " +
                    "check_in_date DATE, " +
                    "check_out_date DATE, " +
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
        Client client = new Client("John", "Doe", "john.doe@example.com", "+1234567890", null);
        Client created = clientDAO.create(client);
        
        assertNotNull(created.getId(), "Client should have an ID after creation");
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
        assertEquals("john.doe@example.com", created.getEmail());
    }

    @Test
    void testCreateWithRoom() throws SQLException {
        Room room = new Room("101", "Standard",
                new java.math.BigDecimal("50.00"), true);
        room = roomDAO.create(room);
        
        Client client = new Client("Jane", "Smith", "jane.smith@example.com", "+0987654321", room.getId());
        client.setCheckInDate(LocalDate.now());
        client.setCheckOutDate(LocalDate.now().plusDays(3));
        
        Client created = clientDAO.create(client);
        assertNotNull(created.getId());
        assertEquals(room.getId(), created.getRoomId());
        assertNotNull(created.getCheckInDate());
        assertNotNull(created.getCheckOutDate());
    }

    @Test
    void testFindById() throws SQLException {
        Client client = new Client("Alice", "Brown", "alice.brown@example.com", "+1111111111", null);
        client = clientDAO.create(client);
        
        Client found = clientDAO.findById(client.getId());
        assertNotNull(found);
        assertEquals(client.getId(), found.getId());
        assertEquals("Alice", found.getFirstName());
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        Client found = clientDAO.findById(999);
        assertNull(found);
    }

    @Test
    void testFindAll() throws SQLException {
        clientDAO.create(new Client("Client1", "Last1", "client1@example.com", "+111", null));
        clientDAO.create(new Client("Client2", "Last2", "client2@example.com", "+222", null));
        
        List<Client> clients = clientDAO.findAll();
        assertNotNull(clients);
        assertTrue(clients.size() >= 2);
    }

    @Test
    void testUpdate() throws SQLException {
        Client client = new Client("Bob", "Wilson", "bob.wilson@example.com", "+3333333333", null);
        client = clientDAO.create(client);
        
        client.setFirstName("Robert");
        client.setEmail("robert.wilson@example.com");
        client.setPhone("+4444444444");
        
        Client updated = clientDAO.update(client);
        assertEquals("Robert", updated.getFirstName());
        assertEquals("robert.wilson@example.com", updated.getEmail());
        assertEquals("+4444444444", updated.getPhone());
    }

    @Test
    void testDelete() throws SQLException {
        Client client = new Client("Charlie", "Davis", "charlie.davis@example.com", "+5555555555", null);
        client = clientDAO.create(client);
        
        boolean deleted = clientDAO.delete(client.getId());
        assertTrue(deleted);
        
        Client found = clientDAO.findById(client.getId());
        assertNull(found);
    }

    @Test
    void testDeleteNotFound() throws SQLException {
        boolean deleted = clientDAO.delete(999);
        assertFalse(deleted);
    }
}

