package com.busbooking.system.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record RouteResponse(
        Integer routeId,
        String originCity,
        String destinationCity,
        BigDecimal distanceKm,
        Integer estimatedDurationMinutes,
        Boolean isActive,
        ZonedDateTime createdAt,
        List<RouteStopResponse> stops
) {}