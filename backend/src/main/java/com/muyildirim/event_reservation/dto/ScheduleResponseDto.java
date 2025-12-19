package com.muyildirim.event_reservation.dto;


import com.muyildirim.event_reservation.model.Schedule;

import java.time.LocalDateTime;

public class ScheduleResponseDto {
    private Long id;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static ScheduleResponseDto from(Schedule s) {
        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.id = s.getId();
        dto.roomId = (s.getRoom() == null ? null : s.getRoom().getId());
        dto.startTime = s.getStartTime();
        dto.endTime = s.getEndTime();
        return dto;
    }

    public Long getId() { return id; }
    public Long getRoomId() { return roomId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
