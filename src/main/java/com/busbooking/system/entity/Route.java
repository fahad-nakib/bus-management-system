package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "routes", // [cite: 34]
        uniqueConstraints = @UniqueConstraint(columnNames = {"origin_city", "destination_city"}) // [cite: 35]
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId; // [cite: 35]

    @Column(name = "origin_city", length = 100, nullable = false)
    private String originCity; // [cite: 35]

    @Column(name = "destination_city", length = 100, nullable = false)
    private String destinationCity; // [cite: 35]

    @Column(name = "distance_km", precision = 6, scale = 2)
    private BigDecimal distanceKm; // [cite: 35]

    @Column(name = "estimated_duration_minutes", nullable = false)
    private Integer estimatedDurationMinutes; // [cite: 35]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // [cite: 35]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 35]
}
