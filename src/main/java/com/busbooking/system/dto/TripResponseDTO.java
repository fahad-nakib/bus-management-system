package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.TripStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public record TripResponseDTO(
        Long tripId,
        Integer busId,
        String busNumber,
        Integer routeId,
        String originCity,
        String destinationCity,
        LocalDate journeyDate,
        LocalTime departureTime,
        LocalTime arrivalTime,
        Integer durationMinutes,
        BigDecimal regularPrice,
        BigDecimal offerPrice,
        Short totalSeats,
        Short availableSeats,
        TripStatusEnum tripStatus,
        Long createdByUserId,
        ZonedDateTime createdAt,
        List<TripStopResponseDTO> stops
) {}