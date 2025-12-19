package com.muyildirim.event_reservation.dto;


import com.muyildirim.event_reservation.model.Room;

public class RoomResponseDto {
    private Long id;
    private String name;
    private int capacity;
    private String type;
    private String location;

    public static RoomResponseDto from(Room r) {
        RoomResponseDto dto = new RoomResponseDto();
        dto.id = r.getId();
        dto.name = r.getName();
        dto.capacity = r.getCapacity();
        dto.type = r.getType();
        dto.location = r.getLocation();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public String getType() { return type; }
    public String getLocation() { return location; }
}

