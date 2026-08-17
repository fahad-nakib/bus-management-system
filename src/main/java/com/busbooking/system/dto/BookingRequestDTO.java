package com.busbooking.system.dto;

import java.util.List;

public record BookingRequestDTO(
        Long tripId,
        Long userId,
        String sessionId,
        List<Long> tripSeatIds,
        List<PassengerDTO> passengers,
        int boardingPointId,
        int droppingPointId,
        String contactPhone,
        String contactEmail
) {}