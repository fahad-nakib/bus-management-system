package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_passengers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Long passengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(name = "age")
    private Short age;

    @Column(name = "gender", length = 20)
    private GenderEnum gender;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
}