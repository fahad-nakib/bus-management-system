package com.busbooking.system.dto;

import java.util.List;

public record RouteStopResponse(
        Integer routeStopId,
        Integer routeId,
        String stopName,
        String city,
        Short stopOrder,
        List<BoardingDroppingPointResponse> points
) {}