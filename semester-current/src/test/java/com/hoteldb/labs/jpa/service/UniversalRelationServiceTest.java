package com.hoteldb.labs.jpa.service;

import com.hoteldb.labs.jpa.entity.ClientEntity;
import com.hoteldb.labs.jpa.entity.RoomEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UniversalRelationServiceTest {
    private RoomService roomService;
    private ClientService clientService;
    private UniversalRelationService relationService;

    @BeforeEach
    void setUp() {
        roomService = new RoomService("testPU");
        clientService = new ClientService("testPU");
        relationService = new UniversalRelationService("testPU");
    }

    @AfterEach
    void tearDown() {
        relationService.close();
        roomService.close();
        clientService.close();
    }

    @Test
    void testGetUniversalRelationReturnsRows() {
        RoomEntity room = roomService.create(new RoomEntity("UR-1", "Standard", new BigDecimal("10.00"), true));
        ClientEntity client = new ClientEntity("U", "R", "ur@example.com", "+1", room.getId());
        client.setCheckInDate(LocalDate.now());
        clientService.create(client);

        List<Object[]> rows = relationService.getUniversalRelation();
        assertNotNull(rows);
        assertTrue(rows.size() >= 1);
        assertNotNull(rows.getFirst());
    }

    @Test
    void testCloseIsIdempotent() {
        relationService.close();
        assertDoesNotThrow(() -> relationService.close());
    }
}

