package com.busbooking.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BusLayoutRequestDTO {

    @NotBlank(message = "Layout name is required")
    @Size(max = 100, message = "Layout name must not exceed 100 characters")
    private String layoutName; // [cite: 22]

    @NotBlank(message = "Layout type is required (SINGLE_DECK, DOUBLE_DECK)")
    private String layoutType; // [cite: 22, 64]

    @NotNull(message = "Total seats count is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Short totalSeats; // [cite: 22]

    @NotNull(message = "Lower deck presence must be specified")
    private Boolean hasLowerDeck; // [cite: 22]

    @NotNull(message = "Upper deck presence must be specified")
    private Boolean hasUpperDeck; // [cite: 22]

    @Min(value = 1, message = "Rows in lower deck must be at least 1")
    private Short rowsLower; // [cite: 22]

    private Short rowsUpper; // [cite: 22]

    @NotNull(message = "Columns per row is required")
    @Min(value = 1, message = "Columns per row must be at least 1")
    private Short columnsPerRow; //
}