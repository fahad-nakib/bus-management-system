package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_passengers") // [cite: 58]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Long passengerId; // [cite: 58]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking; // [cite: 58]

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName; // [cite: 58]

    @Column(name = "age")
    private Short age; // [cite: 58]

    @Column(name = "gender", length = 20)
    private String gender; // [cite: 58]

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // [cite: 58]
}