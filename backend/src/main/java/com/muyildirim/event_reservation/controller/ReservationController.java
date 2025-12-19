package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.dto.CreateReservationRequest;
import com.muyildirim.event_reservation.dto.ReservationResponseDto;
import com.muyildirim.event_reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ✅ 1) Rezervasyon oluştur (USER) -> DTO döner
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto create(@Valid @RequestBody CreateReservationRequest req, Principal principal) {
        return reservationService.createReservationDto(req, principal);
    }

    // ✅ 2) Kendi rezervasyonlarım (USER) -> DTO döner
    @GetMapping("/my")
    public List<ReservationResponseDto> my(Principal principal) {
        return reservationService.myReservationsDto(principal);
    }

    // ✅ 3) Kendi PENDING rezervasyonunu iptal et (USER) -> CANCELLED
    @DeleteMapping("/{id}")
    public Map<String, Object> cancel(@PathVariable Long id, Principal principal) {
        reservationService.cancelReservation(id, principal);
        return Map.of(
                "message", "Reservation cancelled",
                "id", id,
                "status", "CANCELLED"
        );
    }
}
