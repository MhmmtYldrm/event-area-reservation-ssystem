package com.muyildirim.event_reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muyildirim.event_reservation.model.Reservation;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponseDto {

    private Long id;
    private String status; // 🔥 enum yerine string (JSON temizliği)

    private Long userId;
    private String userEmail;
    private String userFullName;

    private Long roomId;
    private String roomName;

    private Long scheduleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // ✅ Event bilgileri
    private Long eventId;
    private String eventTitle;
    private String eventDescription;

    public static ReservationResponseDto from(Reservation r) {
        ReservationResponseDto dto = new ReservationResponseDto();

        dto.id = r.getId();
        dto.status = r.getStatus() != null ? r.getStatus().name() : null;

        if (r.getUser() != null) {
            dto.userId = r.getUser().getId();
            dto.userEmail = r.getUser().getEmail();
            dto.userFullName = r.getUser().getFullName();
        }

        if (r.getRoom() != null) {
            dto.roomId = r.getRoom().getId();
            dto.roomName = r.getRoom().getName();
        }

        if (r.getSchedule() != null) {
            dto.scheduleId = r.getSchedule().getId();
            dto.startTime = r.getSchedule().getStartTime();
            dto.endTime = r.getSchedule().getEndTime();
        }

        // ✅ Event mapping
        if (r.getEvent() != null) {
            dto.eventId = r.getEvent().getId();
            dto.eventTitle = r.getEvent().getTitle();
            dto.eventDescription = r.getEvent().getDescription();
        }

        return dto;
    }

    // getters
    public Long getId() { return id; }
    public String getStatus() { return status; }

    public Long getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public String getUserFullName() { return userFullName; }

    public Long getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }

    public Long getScheduleId() { return scheduleId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public Long getEventId() { return eventId; }
    public String getEventTitle() { return eventTitle; }
    public String getEventDescription() { return eventDescription; }
}
