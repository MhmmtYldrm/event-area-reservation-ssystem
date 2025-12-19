package com.muyildirim.event_reservation.service;

import com.muyildirim.event_reservation.dto.CreateReservationRequest;
import com.muyildirim.event_reservation.dto.ReservationResponseDto;
import com.muyildirim.event_reservation.model.*;
import com.muyildirim.event_reservation.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            RoomRepository roomRepository,
            ScheduleRepository scheduleRepository,
            EventRepository eventRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
        this.eventRepository = eventRepository;
    }

    private String requireEmail(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return principal.getName();
    }

    private User requireUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Room requireRoom(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "roomId is required");
        }
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
    }

    private Schedule requireSchedule(Long scheduleId) {
        if (scheduleId == null || scheduleId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "scheduleId is required");
        }
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));
    }

    private Event requireEvent(Long eventId) {
        if (eventId == null || eventId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "eventId is required");
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    // =========================
    // USER
    // =========================

    @Transactional
    public Reservation createReservation(CreateReservationRequest req, Principal principal) {
        String email = requireEmail(principal);
        User user = requireUserByEmail(email);

        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }

        Room room = requireRoom(req.getRoomId());
        Schedule schedule = requireSchedule(req.getScheduleId());
        Event event = requireEvent(req.getEventId());

        // schedule -> room uyumu
        if (schedule.getRoom() == null || schedule.getRoom().getId() == null ||
                !schedule.getRoom().getId().equals(room.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule does not belong to this room");
        }

        // çakışma kontrolü
        boolean conflict = reservationRepository.existsBySchedule_IdAndStatusIn(
                schedule.getId(),
                List.of(ReservationStatus.PENDING, ReservationStatus.APPROVED)
        );
        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This schedule is already reserved");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setSchedule(schedule);
        reservation.setEvent(event);
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> myReservations(Principal principal) {
        String email = requireEmail(principal);
        // Not: Repository query'inde event fetch'li versiyonunu kullanırsan dto'da eventTitle null kalmaz.
        return reservationRepository.findMyWithDetails(email);
    }

    @Transactional
    public void cancelMyPendingReservation(Long reservationId, Principal principal) {
        String email = requireEmail(principal);

        if (reservationId == null || reservationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id");
        }

        Reservation reservation = reservationRepository
                .findByIdAndUserEmailAndStatus(reservationId, email, ReservationStatus.PENDING)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "PENDING reservation not found for this user"
                ));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    // Eski controller çağrıları kırılmasın
    @Transactional
    public void cancelReservation(Long reservationId, Principal principal) {
        cancelMyPendingReservation(reservationId, principal);
    }

    // =========================
    // DTO
    // =========================

    @Transactional
    public ReservationResponseDto createReservationDto(CreateReservationRequest req, Principal principal) {
        return ReservationResponseDto.from(createReservation(req, principal));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> myReservationsDto(Principal principal) {
        return myReservations(principal).stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN
    // =========================

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> adminListAll() {
        return reservationRepository.findAllWithDetails()
                .stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> adminApprove(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id");
        }

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING can be approved");
        }

        if (r.getSchedule() == null || r.getSchedule().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has no schedule");
        }

        boolean conflict = reservationRepository.existsBySchedule_IdAndStatusInAndIdNot(
                r.getSchedule().getId(),
                List.of(ReservationStatus.PENDING, ReservationStatus.APPROVED),
                r.getId()
        );
        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This schedule already has another reservation");
        }

        r.setStatus(ReservationStatus.APPROVED);
        reservationRepository.save(r);

        return Map.of("message", "Reservation approved", "id", reservationId);
    }

    @Transactional
    public Map<String, Object> adminReject(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id");
        }

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING can be rejected");
        }

        r.setStatus(ReservationStatus.REJECTED);
        reservationRepository.save(r);

        return Map.of("message", "Reservation rejected", "id", reservationId);
    }
}
