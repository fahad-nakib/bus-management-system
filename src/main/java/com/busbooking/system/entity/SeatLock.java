package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.LockStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "seat_locks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lock_id")
    private Long lockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_seat_id", nullable = false)
    private TripSeat tripSeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "locked_at", nullable = false, updatable = false)
    private ZonedDateTime lockedAt = ZonedDateTime.now();

    @Column(name = "locked_until", nullable = false)
    private ZonedDateTime lockedUntil;

    @Column(name = "lock_status", length = 30, nullable = false)
    private LockStatusEnum lockStatus;

    @Column(name = "released_at")
    private ZonedDateTime releasedAt;
}