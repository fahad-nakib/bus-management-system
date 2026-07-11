package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boarding_dropping_points") // [cite: 41]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardingDroppingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Integer pointId; // [cite: 44]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_stop_id", nullable = false)
    private RouteStop routeStop; // [cite: 44]

    @Column(name = "point_name", length = 150, nullable = false)
    private String pointName; // [cite: 44]

    @Column(name = "point_type", length = 20, nullable = false)
    private String pointType; // [cite: 44, 64]

    @Column(name = "address", columnDefinition = "TEXT")
    private String address; // [cite: 44]

    @Column(name = "landmark", length = 150)
    private String landmark; // [cite: 44]

    @Column(name = "contact_number", length = 20)
    private String contactNumber; // [cite: 44]

    @Column(name = "default_offset_minutes", nullable = false)
    private Integer defaultOffsetMinutes; // [cite: 44]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // [cite: 44]
}