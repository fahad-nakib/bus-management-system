package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.SeatTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"layout_id", "seat_label"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private BusLayout busLayout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Column(name = "seat_label", length = 10, nullable = false)
    private String seatLabel;

    @Column(name = "row_number", nullable = false)
    private Short rowNumber;

    @Column(name = "column_number", nullable = false)
    private Short columnNumber;

    @Column(name = "seat_type", length = 30, nullable = false)
    private SeatTypeEnum seatType;

    @Column(name = "is_ladies_seat", nullable = false)
    private Boolean isLadiesSeat = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
