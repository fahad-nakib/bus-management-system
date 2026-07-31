package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.PointTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boarding_dropping_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardingDroppingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Integer pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_stop_id", nullable = false)
    private RouteStop routeStop;

    @Column(name = "point_name", length = 150, nullable = false)
    private String pointName;

    @Column(name = "point_type", length = 20, nullable = false)
    private PointTypeEnum pointType;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "landmark", length = 150)
    private String landmark;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "default_offset_minutes", nullable = false)
    private Integer defaultOffsetMinutes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}