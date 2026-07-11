package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.TripStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "trips") // [cite: 45]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId; // [cite: 48]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus; // [cite: 48]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route; // [cite: 48]

    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate; // [cite: 48]

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime; // [cite: 48]

    @Column(name = "arrival_time")
    private LocalTime arrivalTime; // [cite: 48]

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes; // [cite: 48]

    @Column(name = "regular_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal regularPrice; // [cite: 48]

    @Column(name = "offer_price", precision = 10, scale = 2)
    private BigDecimal offerPrice; // [cite: 48]

    @Column(name = "total_seats", nullable = false)
    private Short totalSeats; // [cite: 48]

    @Column(name = "available_seats", nullable = false)
    private Short availableSeats; // [cite: 48]

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status", nullable = false)
    private TripStatusEnum tripStatus; // [cite: 48, 64]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // [cite: 48]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 48]

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt; // [cite: 48]
}