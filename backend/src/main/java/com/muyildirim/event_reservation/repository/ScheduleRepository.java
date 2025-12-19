package com.muyildirim.event_reservation.repository;

import com.muyildirim.event_reservation.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // ✅ Room'a göre schedule listeleme
    List<Schedule> findByRoom_IdOrderByStartTimeAsc(Long roomId);

    // ✅ AdminScheduleController create() içinde kullandık: "aynı slot var mı?"
    boolean existsByRoom_IdAndStartTimeAndEndTime(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    // ✅ Overlap / çakışma kontrolü
    @Query("""
        select (count(s) > 0) from Schedule s
        where s.room.id = :roomId
          and (s.startTime < :endTime and s.endTime > :startTime)
    """)
    boolean existsOverlapping(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // ✅ 🔥 ADMIN DASHBOARD İÇİN GEREKLİ
    // Tüm schedule'ları kronolojik sırayla getirir
    List<Schedule> findAllByOrderByStartTimeAsc();
}
