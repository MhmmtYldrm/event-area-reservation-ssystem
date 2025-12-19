package com.muyildirim.event_reservation.repository;

import com.muyildirim.event_reservation.model.Reservation;
import com.muyildirim.event_reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserEmailOrderByIdDesc(String email);

    @Query("""
        select r from Reservation r
        join fetch r.user u
        left join fetch r.room
        left join fetch r.schedule
        left join fetch r.event
        where u.email = :email
        order by r.id desc
    """)
    List<Reservation> findMyWithDetails(@Param("email") String email);

    boolean existsBySchedule_IdAndStatusIn(Long scheduleId, List<ReservationStatus> statuses);

    boolean existsBySchedule_IdAndStatusInAndIdNot(Long scheduleId, List<ReservationStatus> statuses, Long id);

    Optional<Reservation> findByIdAndUserEmailAndStatus(Long id, String email, ReservationStatus status);

    List<Reservation> findAllByOrderByIdDesc();

    @Query("""
        select r from Reservation r
        join fetch r.user
        left join fetch r.room
        left join fetch r.schedule
        left join fetch r.event
        order by r.id desc
    """)
    List<Reservation> findAllWithDetails();
}
