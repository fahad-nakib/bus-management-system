package com.busbooking.system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record TripScheduleRequestDTO(
        Integer busId,
        Integer routeId,
        LocalDate journeyDate,
        LocalTime departureTime,
        BigDecimal regularPrice,
        BigDecimal offerPrice,
        Long createdByUserId
) {}