package com.busbooking.system.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class BusLayoutResponseDTO {
    private Integer layoutId; // [cite: 22]
    private String layoutName; // [cite: 22]
    private String layoutType; // [cite: 22, 64]
    private Short totalSeats; // [cite: 22]
    private Boolean hasLowerDeck; // [cite: 22]
    private Boolean hasUpperDeck; // [cite: 22]
    private Short rowsLower; // [cite: 22]
    private Short rowsUpper; // [cite: 22]
    private Short columnsPerRow; //
    private Boolean isActive; //
    private ZonedDateTime createdAt; //
}