package com.hoteldb.spring.service;

import com.hoteldb.spring.domain.RoomEntity;
import com.hoteldb.spring.repository.RoomRepository;
import com.hoteldb.spring.web.dto.RoomForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomManagementService {

    private final RoomRepository roomRepository;

    public RoomManagementService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomEntity> findAll() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RoomEntity requireById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
    }

    @Transactional
    public RoomEntity create(RoomForm form) {
        RoomEntity room = new RoomEntity();
        applyForm(room, form);
        return roomRepository.save(room);
    }

    @Transactional
    public RoomEntity update(Integer id, RoomForm form) {
        RoomEntity room = requireById(id);
        applyForm(room, form);
        return roomRepository.save(room);
    }

    @Transactional
    public void delete(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room not found: " + id);
        }
        roomRepository.deleteById(id);
    }

    private void applyForm(RoomEntity room, RoomForm form) {
        room.setRoomNumber(form.getRoomNumber().trim());
        room.setRoomType(form.getRoomType().trim());
        room.setPricePerNight(form.getPricePerNight());
        room.setIsAvailable(form.isAvailable());
    }
}
