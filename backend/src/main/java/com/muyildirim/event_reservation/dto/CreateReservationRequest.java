package com.muyildirim.event_reservation.dto;

import jakarta.validation.constraints.NotNull;

public class CreateReservationRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private Long scheduleId;

    @NotNull
    private Long eventId;   // 🔥 BU ŞART

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
