package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.dto.CreateEventRequest;
import com.muyildirim.event_reservation.dto.EventResponseDto;
import com.muyildirim.event_reservation.model.Event;
import com.muyildirim.event_reservation.model.User;
import com.muyildirim.event_reservation.repository.EventRepository;
import com.muyildirim.event_reservation.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventController(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<EventResponseDto> list() {
        return eventRepository.findAllWithOrganizer()
                .stream()
                .map(EventResponseDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto create(@Valid @RequestBody CreateEventRequest req, Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User organizer = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Event e = new Event();
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setOrganizer(organizer);

        return EventResponseDto.from(eventRepository.save(e));
    }
}
