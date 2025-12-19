package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.dto.RoomResponseDto;
import com.muyildirim.event_reservation.dto.ScheduleResponseDto;
import com.muyildirim.event_reservation.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
// React tarafı için rahatlık (istersen sonra kaldırır/ayar dosyasına alırsın)
@CrossOrigin(origins = "http://localhost:3000")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // ✅ Rooms list (USER/ADMIN)
    @GetMapping("/rooms")
    public List<RoomResponseDto> rooms() {
        return catalogService.listRooms();
    }

    // ✅ Schedules of a room (USER/ADMIN)
    @GetMapping("/rooms/{roomId}/schedules")
    public List<ScheduleResponseDto> schedules(@PathVariable Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid roomId");
        }
        return catalogService.listSchedulesByRoom(roomId);
    }
}
