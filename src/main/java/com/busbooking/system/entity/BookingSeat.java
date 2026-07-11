package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "booking_seats", // [cite: 58]
        uniqueConstraints = @UniqueConstraint(columnNames = {"trip_seat_id"}) // [cite: 60]
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Long bookingSeatId; // [cite: 60]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking; // [cite: 60]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_seat_id", nullable = false)
    private TripSeat tripSeat; // [cite: 60]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private BookingPassenger passenger; // [cite: 60]

    @Column(name = "seat_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal seatPrice; // [cite: 60]
}