package com.muyildirim.event_reservation.service;

import com.muyildirim.event_reservation.dto.RoomResponseDto;
import com.muyildirim.event_reservation.dto.ScheduleResponseDto;
import com.muyildirim.event_reservation.repository.RoomRepository;
import com.muyildirim.event_reservation.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;

    public CatalogService(RoomRepository roomRepository, ScheduleRepository scheduleRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> listRooms() {
        return roomRepository.findAllByOrderByNameAsc()
                .stream()
                .map(RoomResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> listSchedulesByRoom(Long roomId) {
        return scheduleRepository.findByRoom_IdOrderByStartTimeAsc(roomId)
                .stream()
                .map(ScheduleResponseDto::from)
                .toList();
    }
}
