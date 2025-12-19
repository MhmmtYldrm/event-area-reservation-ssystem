package com.muyildirim.event_reservation.dto;

import java.util.List;

public class AdminDashboardResponseDto {

    private List<EventResponseDto> events;
    private List<RoomResponseDto> rooms;
    private List<ScheduleResponseDto> schedules;
    private List<ReservationResponseDto> reservations;

    public AdminDashboardResponseDto(
            List<EventResponseDto> events,
            List<RoomResponseDto> rooms,
            List<ScheduleResponseDto> schedules,
            List<ReservationResponseDto> reservations
    ) {
        this.events = events;
        this.rooms = rooms;
        this.schedules = schedules;
        this.reservations = reservations;
    }

    public List<EventResponseDto> getEvents() { return events; }
    public List<RoomResponseDto> getRooms() { return rooms; }
    public List<ScheduleResponseDto> getSchedules() { return schedules; }
    public List<ReservationResponseDto> getReservations() { return reservations; }
}
