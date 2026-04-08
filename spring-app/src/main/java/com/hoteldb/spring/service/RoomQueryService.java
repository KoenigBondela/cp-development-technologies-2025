package com.hoteldb.spring.service;

import com.hoteldb.spring.domain.RoomEntity;
import com.hoteldb.spring.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomQueryService {

    private final RoomRepository roomRepository;

    public RoomQueryService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomEntity> findAll() {
        return roomRepository.findAll();
    }
}
