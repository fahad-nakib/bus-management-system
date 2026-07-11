package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "decks", // [cite: 24]
        uniqueConstraints = @UniqueConstraint(columnNames = {"layout_id", "deck_type"}) // [cite: 26]
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Integer deckId; // [cite: 25]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private BusLayout busLayout; // [cite: 25]

    @Column(name = "deck_type", length = 20, nullable = false)
    private String deckType; // [cite: 25, 64]

    @Column(name = "deck_label", length = 10, nullable = false)
    private String deckLabel; // [cite: 25]
}