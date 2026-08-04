package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "route_stops",
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "stop_order"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_stop_id")
    private Integer routeStopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "stop_name", length = 150, nullable = false)
    private String stopName;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "stop_order", nullable = false)
    private Short stopOrder;
}