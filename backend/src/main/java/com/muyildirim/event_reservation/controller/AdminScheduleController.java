package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.dto.ScheduleResponseDto;
import com.muyildirim.event_reservation.model.Room;
import com.muyildirim.event_reservation.model.Schedule;
import com.muyildirim.event_reservation.repository.RoomRepository;
import com.muyildirim.event_reservation.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms/{roomId}/schedules")
public class AdminScheduleController {

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;

    public AdminScheduleController(RoomRepository roomRepository,
                                   ScheduleRepository scheduleRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // ✅ Admin → room’a slot ekle (DTO request)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponseDto create(@PathVariable Long roomId, @RequestBody CreateScheduleRequest req) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (req == null || req.startTime == null || req.endTime == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime and endTime are required");
        }
        if (!req.endTime.isAfter(req.startTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endTime must be after startTime");
        }

        // (opsiyonel ama faydalı) aynı room içinde birebir aynı slot var mı?
        boolean exactExists = scheduleRepository
                .existsByRoom_IdAndStartTimeAndEndTime(roomId, req.startTime, req.endTime);
        if (exactExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This schedule already exists for this room");
        }

        Schedule schedule = new Schedule();
        schedule.setId(null);
        schedule.setRoom(room);
        schedule.setStartTime(req.startTime);
        schedule.setEndTime(req.endTime);

        return ScheduleResponseDto.from(scheduleRepository.save(schedule));
    }

    // ✅ Admin → room’un slotlarını gör (DTO response)
    @GetMapping
    public List<ScheduleResponseDto> list(@PathVariable Long roomId) {
        // room yoksa 404 dönmek istersen aç:
        if (!roomRepository.existsById(roomId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }

        return scheduleRepository.findByRoom_IdOrderByStartTimeAsc(roomId)
                .stream()
                .map(ScheduleResponseDto::from)
                .toList();
    }

    // ✅ Admin → slot sil (CRUD örneği için çok iyi)
    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long roomId, @PathVariable Long scheduleId) {
        Schedule s = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        if (s.getRoom() == null || s.getRoom().getId() == null || !s.getRoom().getId().equals(roomId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule does not belong to this room");
        }

        scheduleRepository.deleteById(scheduleId);
    }

    // ✅ küçük request DTO (ister ayrı dosyaya taşırsın)
    public static class CreateScheduleRequest {
        public LocalDateTime startTime;
        public LocalDateTime endTime;
    }
}
