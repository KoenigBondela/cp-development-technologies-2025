package com.hoteldb.spring.api;

import com.hoteldb.spring.api.dto.RoomResponse;
import com.hoteldb.spring.service.RoomQueryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomRestController {

    private final RoomQueryService roomQueryService;

    public RoomRestController(RoomQueryService roomQueryService) {
        this.roomQueryService = roomQueryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomResponse> listAll() {
        return roomQueryService.findAll().stream().map(RoomResponse::from).toList();
    }
}
