package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "counters") // [cite: 14]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counter_id")
    private Integer counterId; // [cite: 15]

    @Column(name = "counter_name", length = 100, nullable = false)
    private String counterName; // [cite: 15]

    @Column(name = "counter_code", length = 20, unique = true, nullable = false)
    private String counterCode; // [cite: 15]

    @Column(name = "address", columnDefinition = "TEXT")
    private String address; // [cite: 15]

    @Column(name = "city", length = 100)
    private String city; // [cite: 15]

    @Column(name = "contact_number", length = 20)
    private String contactNumber; // [cite: 15]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user_id")
    private User manager; // [cite: 15]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // [cite: 15]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 15]
}