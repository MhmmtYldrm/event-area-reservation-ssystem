package com.muyildirim.event_reservation.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 name null olmasın (roomName sorunu bir daha yaşanmasın)
    @Column(nullable = false, length = 100)
    private String name;

    // kapasite null olmasın
    @Column(nullable = false)
    private int capacity;

    @Column(length = 50)
    private String type;

    @Column(length = 120)
    private String location;
}
