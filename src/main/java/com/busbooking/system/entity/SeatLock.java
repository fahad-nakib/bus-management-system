package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "seat_locks") // [cite: 55]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lock_id")
    private Long lockId; // [cite: 56]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_seat_id", nullable = false)
    private TripSeat tripSeat; // [cite: 56]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // [cite: 56]

    @Column(name = "session_id", length = 100)
    private String sessionId; // [cite: 56]

    @Column(name = "locked_at", nullable = false, updatable = false)
    private ZonedDateTime lockedAt = ZonedDateTime.now(); // [cite: 56]

    @Column(name = "locked_until", nullable = false)
    private ZonedDateTime lockedUntil; // [cite: 56]

    @Column(name = "lock_status", length = 30, nullable = false)
    private String lockStatus; // [cite: 56, 64]

    @Column(name = "released_at")
    private ZonedDateTime releasedAt; // [cite: 56]
}