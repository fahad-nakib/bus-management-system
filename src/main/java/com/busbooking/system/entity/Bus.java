package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "buses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Integer busId;

    @Column(name = "bus_number", length = 20, unique = true, nullable = false)
    private String busNumber;

    @Column(name = "registration_number", length = 30, unique = true, nullable = false)
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private BusLayout busLayout;

    @Column(name = "ac_type", length = 20, nullable = false)
    private String acType;

    @Column(name = "class_type", length = 30, nullable = false)
    private String classType;

    @Column(name = "manufacturer", length = 50)
    private String manufacturer;

    @Column(name = "model_year")
    private Short modelYear;

    @Column(name = "total_seats", nullable = false)
    private Short totalSeats;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
