package com.busbooking.system.dto;

import java.math.BigDecimal;
import java.util.List;

public record RouteCreateRequest(
        String originCity,
        String destinationCity,
        BigDecimal distanceKm,
        Integer estimatedDurationMinutes,
        Boolean isActive,
        List<RouteStopRequest> stops
) {}