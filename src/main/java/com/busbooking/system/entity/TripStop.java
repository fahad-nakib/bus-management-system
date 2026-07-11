package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "trip_stops") // [cite: 49]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_stop_id")
    private Long tripStopId; // [cite: 51]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip; // [cite: 51]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false)
    private BoardingDroppingPoint boardingDroppingPoint; // [cite: 51]

    @Column(name = "stop_order", nullable = false)
    private Short stopOrder; // [cite: 51]

    @Column(name = "scheduled_time", nullable = false)
    private LocalTime scheduledTime; // [cite: 51]

    @Column(name = "stop_role", length = 20, nullable = false)
    private String stopRole; // [cite: 51, 64]
}
