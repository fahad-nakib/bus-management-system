package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.SeatStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "trip_seats", // [cite: 53]
        uniqueConstraints = @UniqueConstraint(columnNames = {"trip_id", "seat_id"}) // [cite: 55]
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_seat_id")
    private Long tripSeatId; // [cite: 55]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip; // [cite: 55]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat; // [cite: 55]

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status", nullable = false)
    private SeatStatusEnum seatStatus; // [cite: 55, 64]

    @Column(name = "price_override", precision = 10, scale = 2)
    private BigDecimal priceOverride; // [cite: 55]

    @Version // Optimistic locking
    @Column(name = "version", nullable = false)
    private Integer version = 0; // [cite: 55]

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt; // [cite: 55]
}