package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.BookingChannelEnum;
import com.busbooking.system.entity.enums.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "bookings") // [cite: 56]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId; // [cite: 56]

    @Column(name = "booking_reference", length = 20, unique = true, nullable = false)
    private String bookingReference; // [cite: 56]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip; // [cite: 57]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // [cite: 57]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counter_id")
    private Counter counter; // [cite: 57]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_by_agent_id")
    private User bookedByAgent; // [cite: 57]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boarding_point_id", nullable = false)
    private BoardingDroppingPoint boardingPoint; // [cite: 57]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropping_point_id", nullable = false)
    private BoardingDroppingPoint droppingPoint; // [cite: 57]

    @Column(name = "booking_channel", length = 30, nullable = false)
    private BookingChannelEnum bookingChannel; // [cite: 58]

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatusEnum bookingStatus; // [cite: 58]

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount; // [cite: 58]

    @Column(name = "contact_phone", length = 20, nullable = false)
    private String contactPhone; // [cite: 58]

    @Column(name = "contact_email", length = 150)
    private String contactEmail; // [cite: 58]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 58]

    @Column(name = "confirmed_at")
    private ZonedDateTime confirmedAt; // [cite: 58]

    @Column(name = "cancelled_at")
    private ZonedDateTime cancelledAt; // [cite: 58]

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt; // [cite: 58]
}