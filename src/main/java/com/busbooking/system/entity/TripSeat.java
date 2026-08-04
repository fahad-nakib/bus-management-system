package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.SeatStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "trip_seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"trip_id", "seat_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_seat_id")
    private Long tripSeatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status", nullable = false)
    private SeatStatusEnum seatStatus;

    @Column(name = "price_override", precision = 10, scale = 2)
    private BigDecimal priceOverride;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}