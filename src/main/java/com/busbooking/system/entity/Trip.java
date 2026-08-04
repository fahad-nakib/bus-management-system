package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.TripStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "trips",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_bus_journey_departure",
                        columnNames = {"bus_id", "journey_date", "departure_time"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "regular_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal regularPrice;

    @Column(name = "offer_price", precision = 10, scale = 2)
    private BigDecimal offerPrice;

    @Column(name = "total_seats", nullable = false)
    private Short totalSeats;

    @Column(name = "available_seats", nullable = false)
    private Short availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status", nullable = false)
    private TripStatusEnum tripStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}