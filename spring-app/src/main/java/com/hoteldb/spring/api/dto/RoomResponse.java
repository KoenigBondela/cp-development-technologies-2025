package com.hoteldb.spring.api.dto;

import com.hoteldb.spring.domain.RoomEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RoomResponse(
        Integer id,
        String roomNumber,
        String roomType,
        BigDecimal pricePerNight,
        boolean available,
        LocalDateTime createdAt) {

    public static RoomResponse from(RoomEntity r) {
        return new RoomResponse(
                r.getId(),
                r.getRoomNumber(),
                r.getRoomType(),
                r.getPricePerNight(),
                Boolean.TRUE.equals(r.getIsAvailable()),
                r.getCreatedAt());
    }
}
