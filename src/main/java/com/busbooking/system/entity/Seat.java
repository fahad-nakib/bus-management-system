package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "seats", // [cite: 27]
        uniqueConstraints = @UniqueConstraint(columnNames = {"layout_id", "seat_label"}) // [cite: 30, 31]
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId; // [cite: 28]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private BusLayout busLayout; // [cite: 28]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck; // [cite: 28]

    @Column(name = "seat_label", length = 10, nullable = false)
    private String seatLabel; // [cite: 28]

    @Column(name = "row_number", nullable = false)
    private Short rowNumber; // [cite: 28]

    @Column(name = "column_number", nullable = false)
    private Short columnNumber; // [cite: 28]

    @Column(name = "seat_type", length = 30, nullable = false)
    private String seatType; // [cite: 28, 64]

    @Column(name = "is_ladies_seat", nullable = false)
    private Boolean isLadiesSeat = false; // [cite: 29]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // [cite: 29]
}
