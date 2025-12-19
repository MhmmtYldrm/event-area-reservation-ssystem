package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.dto.ReservationResponseDto;
import com.muyildirim.event_reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ✅ Admin tüm rezervasyonları görür
    @GetMapping
    public List<ReservationResponseDto> all() {
        return reservationService.adminListAll();
    }

    // ✅ Admin approve (action endpoint -> POST daha net)
    @PostMapping("/{id}/approve")
    public Map<String, Object> approve(@PathVariable Long id) {
        return reservationService.adminApprove(id);
    }

    // ✅ Admin reject
    @PostMapping("/{id}/reject")
    public Map<String, Object> reject(@PathVariable Long id) {
        return reservationService.adminReject(id);
    }
}
