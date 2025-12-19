package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.model.Room;
import com.muyildirim.event_reservation.repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/rooms") // ✅ artık /api/rooms ile ÇAKIŞMAZ
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Debug/Internal: entity list (istersen tamamen silebilirsin)
    @GetMapping
    public List<Room> getAllRoomsRaw() {
        return roomRepository.findAll();
    }
}
