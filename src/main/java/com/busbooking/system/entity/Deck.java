package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.DeckTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "decks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"layout_id", "deck_type"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Integer deckId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", nullable = false)
    private BusLayout busLayout;

    @Column(name = "deck_type", length = 20, nullable = false)
    private DeckTypeEnum deckType;

    @Column(name = "deck_label", length = 10, nullable = false)
    private String deckLabel;
}