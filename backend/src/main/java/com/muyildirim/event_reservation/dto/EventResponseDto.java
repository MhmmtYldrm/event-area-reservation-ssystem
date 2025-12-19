package com.muyildirim.event_reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muyildirim.event_reservation.model.Event;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponseDto {

    private Long id;
    private String title;
    private String description;

    private Long organizerId;
    private String organizerEmail;
    private String organizerFullName;

    public static EventResponseDto from(Event e) {
        EventResponseDto dto = new EventResponseDto();
        dto.id = e.getId();
        dto.title = e.getTitle();
        dto.description = e.getDescription();

        if (e.getOrganizer() != null) {
            dto.organizerId = e.getOrganizer().getId();
            dto.organizerEmail = e.getOrganizer().getEmail();
            dto.organizerFullName = e.getOrganizer().getFullName();
        }
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Long getOrganizerId() { return organizerId; }
    public String getOrganizerEmail() { return organizerEmail; }
    public String getOrganizerFullName() { return organizerFullName; }
}
