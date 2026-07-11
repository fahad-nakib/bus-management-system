package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "bus_layouts") // [cite: 21]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layout_id")
    private Integer layoutId; // [cite: 22]

    @Column(name = "layout_name", length = 100, nullable = false)
    private String layoutName; // [cite: 22]

    @Column(name = "layout_type", length = 30, nullable = false)
    private String layoutType; // [cite: 22, 64]

    @Column(name = "total_seats", nullable = false)
    private Short totalSeats; // [cite: 22]

    @Column(name = "has_lower_deck", nullable = false)
    private Boolean hasLowerDeck; // [cite: 22]

    @Column(name = "has_upper_deck", nullable = false)
    private Boolean hasUpperDeck; // [cite: 22]

    @Column(name = "rows_lower")
    private Short rowsLower; // [cite: 22]

    @Column(name = "rows_upper")
    private Short rowsUpper; // [cite: 22]

    @Column(name = "columns_per_row")
    private Short columnsPerRow; // [cite: 23]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // [cite: 23]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 23]
}
