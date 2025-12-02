package com.hoteldb.labs.jpa.service;

import com.hoteldb.labs.jpa.entity.ClientEntity;
import com.hoteldb.labs.jpa.entity.RoomEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ClientService (Lab 3)
 */
class ClientServiceTest {
    private ClientService clientService;
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService("testPU");
        roomService = new RoomService("testPU");
    }

    @AfterEach
    void tearDown() {
        if (clientService != null) {
            clientService.close();
        }
        if (roomService != null) {
            roomService.close();
        }
    }

    @Test
    void testCreate() {
        ClientEntity client = new ClientEntity("John", "Doe", "john.doe@example.com", "+1234567890", null);
        ClientEntity created = clientService.create(client);
        
        assertNotNull(created.getId(), "Client should have an ID after creation");
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
    }

    @Test
    void testCreateWithRoom() {
        RoomEntity room = new RoomEntity("101", "Standard", new java.math.BigDecimal("50.00"), true);
        room = roomService.create(room);
        
        ClientEntity client = new ClientEntity("Jane", "Smith", "jane.smith@example.com", "+0987654321", room.getId());
        client.setCheckInDate(LocalDate.now());
        client.setCheckOutDate(LocalDate.now().plusDays(3));
        
        ClientEntity created = clientService.create(client);
        assertNotNull(created.getId());
        assertEquals(room.getId(), created.getRoomId());
    }

    @Test
    void testFindById() {
        ClientEntity client = new ClientEntity("Alice", "Brown", "alice.brown@example.com", "+1111111111", null);
        client = clientService.create(client);
        
        ClientEntity found = clientService.findById(client.getId());
        assertNotNull(found);
        assertEquals(client.getId(), found.getId());
        assertEquals("Alice", found.getFirstName());
    }

    @Test
    void testFindByIdNotFound() {
        ClientEntity found = clientService.findById(999);
        assertNull(found);
    }

    @Test
    void testFindAll() {
        clientService.create(new ClientEntity("Client1", "Last1", "client1@example.com", "+111", null));
        clientService.create(new ClientEntity("Client2", "Last2", "client2@example.com", "+222", null));
        
        List<ClientEntity> clients = clientService.findAll();
        assertNotNull(clients);
        assertTrue(clients.size() >= 2);
    }

    @Test
    void testUpdate() {
        ClientEntity client = new ClientEntity("Bob", "Wilson", "bob.wilson@example.com", "+3333333333", null);
        client = clientService.create(client);
        
        client.setFirstName("Robert");
        client.setEmail("robert.wilson@example.com");
        client.setPhone("+4444444444");
        
        ClientEntity updated = clientService.update(client);
        assertEquals("Robert", updated.getFirstName());
        assertEquals("robert.wilson@example.com", updated.getEmail());
    }

    @Test
    void testDelete() {
        ClientEntity client = new ClientEntity("Charlie", "Davis", "charlie.davis@example.com", "+5555555555", null);
        client = clientService.create(client);
        
        boolean deleted = clientService.delete(client.getId());
        assertTrue(deleted);
        
        ClientEntity found = clientService.findById(client.getId());
        assertNull(found);
    }

    @Test
    void testDeleteNotFound() {
        boolean deleted = clientService.delete(999);
        assertFalse(deleted);
    }

    @Test
    void testClose() {
        clientService.close();
        assertDoesNotThrow(() -> clientService.close());
    }
}

