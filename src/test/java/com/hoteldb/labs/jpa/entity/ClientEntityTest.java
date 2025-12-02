package com.hoteldb.labs.jpa.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ClientEntity (Lab 3)
 */
class ClientEntityTest {
    @Test
    void testConstructor() {
        ClientEntity client = new ClientEntity("John", "Doe", "john@example.com", "+1234567890", 1);
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("+1234567890", client.getPhone());
        assertEquals(1, client.getRoomId());
    }

    @Test
    void testGettersAndSetters() {
        ClientEntity client = new ClientEntity();
        client.setId(1);
        client.setFirstName("Jane");
        client.setLastName("Smith");
        client.setEmail("jane@example.com");
        client.setPhone("+0987654321");
        client.setRoomId(2);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(3);
        LocalDateTime createdAt = LocalDateTime.now();
        client.setCheckInDate(checkIn);
        client.setCheckOutDate(checkOut);
        client.setCreatedAt(createdAt);

        assertEquals(1, client.getId());
        assertEquals("Jane", client.getFirstName());
        assertEquals("Smith", client.getLastName());
        assertEquals("jane@example.com", client.getEmail());
        assertEquals("+0987654321", client.getPhone());
        assertEquals(2, client.getRoomId());
        assertEquals(checkIn, client.getCheckInDate());
        assertEquals(checkOut, client.getCheckOutDate());
        assertEquals(createdAt, client.getCreatedAt());
    }

    @Test
    void testEquals() {
        ClientEntity client1 = new ClientEntity("John", "Doe", "john@example.com", "+123", 1);
        client1.setId(1);
        ClientEntity client2 = new ClientEntity("John", "Doe", "john@example.com", "+123", 1);
        client2.setId(1);
        ClientEntity client3 = new ClientEntity("Jane", "Smith", "jane@example.com", "+456", 2);
        client3.setId(2);

        assertEquals(client1, client2);
        assertNotEquals(client1, client3);
        assertNotEquals(client1, null);
    }

    @Test
    void testHashCode() {
        ClientEntity client1 = new ClientEntity("John", "Doe", "john@example.com", "+123", 1);
        client1.setId(1);
        ClientEntity client2 = new ClientEntity("John", "Doe", "john@example.com", "+123", 1);
        client2.setId(1);

        assertEquals(client1.hashCode(), client2.hashCode());
    }

    @Test
    void testToString() {
        ClientEntity client = new ClientEntity("John", "Doe", "john@example.com", "+1234567890", 1);
        client.setId(1);
        String str = client.toString();
        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
    }
}

