package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "routes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"origin_city", "destination_city"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;

    @Column(name = "origin_city", length = 100, nullable = false)
    private String originCity;

    @Column(name = "destination_city", length = 100, nullable = false)
    private String destinationCity;

    @Column(name = "distance_km", precision = 6, scale = 2)
    private BigDecimal distanceKm; 

    @Column(name = "estimated_duration_minutes", nullable = false)
    private Integer estimatedDurationMinutes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
