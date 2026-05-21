package com.hoteldb.spring.api;

import com.hoteldb.spring.api.dto.RoomResponse;
import com.hoteldb.spring.service.RoomManagementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomRestController {

    private final RoomManagementService roomManagementService;

    public RoomRestController(RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomResponse> listAll() {
        return roomManagementService.findAll().stream().map(RoomResponse::from).toList();
    }
}
