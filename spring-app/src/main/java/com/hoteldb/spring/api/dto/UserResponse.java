package com.hoteldb.spring.api.dto;

import com.hoteldb.spring.domain.UserEntity;

import java.time.LocalDateTime;

public record UserResponse(Integer id, String username, String role, LocalDateTime createdAt) {

    public static UserResponse from(UserEntity u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getRole().name(), u.getCreatedAt());
    }
}
