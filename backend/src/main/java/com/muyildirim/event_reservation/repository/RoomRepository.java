package com.muyildirim.event_reservation.repository;

import com.muyildirim.event_reservation.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // ✅ Rooms listeleme (alfabetik)
    List<Room> findAllByOrderByNameAsc();
}
