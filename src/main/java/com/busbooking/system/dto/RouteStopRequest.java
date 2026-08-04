package com.busbooking.system.dto;

import java.util.List;

public record RouteStopRequest(
        String stopName,
        String city,
        Short stopOrder,
        List<BoardingDroppingPointRequest> points
) {}