package com.busbooking.system.dto;

import java.math.BigDecimal;

public record RouteUpdateRequest(
        String originCity,
        String destinationCity,
        BigDecimal distanceKm,
        Integer estimatedDurationMinutes,
        Boolean isActive
) {}