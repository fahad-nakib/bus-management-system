package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.StopRoleEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "trip_stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_stop_id")
    private Long tripStopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false)
    private BoardingDroppingPoint boardingDroppingPoint;

    @Column(name = "stop_order", nullable = false)
    private Short stopOrder;

    @Column(name = "scheduled_time", nullable = false)
    private LocalTime scheduledTime;

    @Column(name = "stop_role", length = 20, nullable = false)
    private StopRoleEnum stopRole;
}
