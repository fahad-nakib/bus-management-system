package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "buses") // [cite: 32]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Integer busId; // [cite: 33]

    @Column(name = "bus_number", length = 20, unique = true, nullable = false)
    private String busNumber; // [cite: 33]

    @Column(name = "registration_number", length = 30, unique = true, nullable = false)
    private String registrationNumber; // [cite: 33]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private BusLayout busLayout; // [cite: 33]

    @Column(name = "ac_type", length = 20, nullable = false)
    private String acType; // [cite: 33, 64]

    @Column(name = "class_type", length = 30, nullable = false)
    private String classType; // [cite: 33, 64]

    @Column(name = "manufacturer", length = 50)
    private String manufacturer; // [cite: 33]

    @Column(name = "model_year")
    private Short modelYear; // [cite: 33]

    @Column(name = "total_seats", nullable = false)
    private Short totalSeats; // [cite: 33]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // [cite: 33]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 33]
}
