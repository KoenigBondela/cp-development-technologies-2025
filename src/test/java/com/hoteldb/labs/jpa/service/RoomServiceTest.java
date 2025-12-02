package com.hoteldb.labs.jpa.service;

import com.hoteldb.labs.jpa.entity.RoomEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoomService (Lab 3)
 */
class RoomServiceTest {
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomService("testPU");
    }

    @AfterEach
    void tearDown() {
        if (roomService != null) {
            roomService.close();
        }
    }

    @Test
    void testCreate() {
        RoomEntity room = new RoomEntity("101", "Standard", new BigDecimal("50.00"), true);
        RoomEntity created = roomService.create(room);
        
        assertNotNull(created.getId(), "Room should have an ID after creation");
        assertEquals("101", created.getRoomNumber());
        assertEquals("Standard", created.getRoomType());
    }

    @Test
    void testFindById() {
        RoomEntity room = new RoomEntity("102", "Deluxe", new BigDecimal("100.00"), true);
        room = roomService.create(room);
        
        RoomEntity found = roomService.findById(room.getId());
        assertNotNull(found);
        assertEquals(room.getId(), found.getId());
        assertEquals("102", found.getRoomNumber());
    }

    @Test
    void testFindByIdNotFound() {
        RoomEntity found = roomService.findById(999);
        assertNull(found);
    }

    @Test
    void testFindAll() {
        roomService.create(new RoomEntity("201", "Standard", new BigDecimal("50.00"), true));
        roomService.create(new RoomEntity("202", "Deluxe", new BigDecimal("100.00"), false));
        
        List<RoomEntity> rooms = roomService.findAll();
        assertNotNull(rooms);
        assertTrue(rooms.size() >= 2);
    }

    @Test
    void testUpdate() {
        RoomEntity room = new RoomEntity("301", "Standard", new BigDecimal("50.00"), true);
        room = roomService.create(room);
        
        room.setRoomType("Deluxe");
        room.setPricePerNight(new BigDecimal("100.00"));
        room.setIsAvailable(false);
        
        RoomEntity updated = roomService.update(room);
        assertEquals("Deluxe", updated.getRoomType());
        assertEquals(new BigDecimal("100.00"), updated.getPricePerNight());
        assertFalse(updated.getIsAvailable());
    }

    @Test
    void testDelete() {
        RoomEntity room = new RoomEntity("401", "Standard", new BigDecimal("50.00"), true);
        room = roomService.create(room);
        
        boolean deleted = roomService.delete(room.getId());
        assertTrue(deleted);
        
        RoomEntity found = roomService.findById(room.getId());
        assertNull(found);
    }

    @Test
    void testDeleteNotFound() {
        boolean deleted = roomService.delete(999);
        assertFalse(deleted);
    }

    @Test
    void testClose() {
        roomService.close();
        // Should not throw exception
        assertDoesNotThrow(() -> roomService.close());
    }
}

