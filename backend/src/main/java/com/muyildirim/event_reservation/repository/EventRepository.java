package com.muyildirim.event_reservation.repository;

import com.muyildirim.event_reservation.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    // ✅ ADMIN: tüm event'leri organizer ile birlikte getir
    @Query("""
        select e from Event e
        left join fetch e.organizer
        order by e.id desc
    """)
    List<Event> findAllWithOrganizer();

    // ✅ USER/RESERVATION: eventId ile tek event fetch (lazy patlamasın)
    @Query("""
        select e from Event e
        left join fetch e.organizer
        where e.id = :id
    """)
    Optional<Event> findByIdWithOrganizer(@Param("id") Long id);
}
