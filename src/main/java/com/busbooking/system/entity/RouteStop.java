package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "route_stops", // [cite: 36]
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "stop_order"}) // [cite: 39, 40]
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_stop_id")
    private Integer routeStopId; // [cite: 38]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route; // [cite: 38]

    @Column(name = "stop_name", length = 150, nullable = false)
    private String stopName; // [cite: 38]

    @Column(name = "city", length = 100, nullable = false)
    private String city; // [cite: 38]

    @Column(name = "stop_order", nullable = false)
    private Short stopOrder; // [cite: 38]
}