package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.model.Room;
import com.muyildirim.event_reservation.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms")
public class AdminRoomController {

    private final RoomRepository roomRepository;

    public AdminRoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // ✅ Admin: tüm odaları gör
    @GetMapping
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    // ✅ Admin: oda ekle
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room create(@RequestBody Room room) {
        room.setId(null); // client id yollasa bile yeni kayıt aç

        if (room.getName() == null || room.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        return roomRepository.save(room);
    }

    // ✅ Admin: oda güncelle (alanları existing'e taşı)
    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @RequestBody Room incoming) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found: " + id));

        // Room entity: name, capacity, type, location
        if (incoming.getName() != null && !incoming.getName().isBlank()) {
            existing.setName(incoming.getName());
        }
        existing.setCapacity(incoming.getCapacity());

        if (incoming.getType() != null) {
            existing.setType(incoming.getType());
        }
        if (incoming.getLocation() != null) {
            existing.setLocation(incoming.getLocation());
        }

        return roomRepository.save(existing);
    }

    // ✅ Admin: oda sil
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found: " + id);
        }
        roomRepository.deleteById(id);
    }
}
