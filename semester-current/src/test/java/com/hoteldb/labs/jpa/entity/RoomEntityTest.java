package com.hoteldb.labs.jpa.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoomEntity (Lab 3)
 */
class RoomEntityTest {
    @Test
    void testConstructor() {
        RoomEntity room = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        assertEquals("101", room.getRoomNumber());
        assertEquals("Standard", room.getRoomType());
        assertEquals(new BigDecimal("50.00"), room.getPricePerNight());
        assertTrue(room.getIsAvailable());
    }

    @Test
    void testGettersAndSetters() {
        RoomEntity room = new RoomEntity();
        room.setId(1);
        room.setRoomNumber("102");
        room.setRoomType("Deluxe");
        room.setPricePerNight(new BigDecimal("100.00"));
        room.setIsAvailable(false);
        LocalDateTime now = LocalDateTime.now();
        room.setCreatedAt(now);

        assertEquals(1, room.getId());
        assertEquals("102", room.getRoomNumber());
        assertEquals("Deluxe", room.getRoomType());
        assertEquals(new BigDecimal("100.00"), room.getPricePerNight());
        assertFalse(room.getIsAvailable());
        assertEquals(now, room.getCreatedAt());
    }

    @Test
    void testEquals() {
        RoomEntity room1 = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        room1.setId(1);
        RoomEntity room2 = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        room2.setId(1);
        RoomEntity room3 = new RoomEntity("102", "Deluxe", new BigDecimal("100.00"), true);
        room3.setId(2);

        assertEquals(room1, room2);
        assertNotEquals(room1, room3);
        assertNotEquals(room1, null);
    }

    @Test
    void testHashCode() {
        RoomEntity room1 = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        room1.setId(1);
        RoomEntity room2 = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        room2.setId(1);

        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    void testToString() {
        RoomEntity room = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        room.setId(1);
        String str = room.toString();
        assertTrue(str.contains("101"));
        assertTrue(str.contains("Standard"));
    }
}

