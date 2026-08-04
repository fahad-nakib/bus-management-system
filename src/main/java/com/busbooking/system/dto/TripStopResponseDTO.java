package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.StopRoleEnum;
import java.time.LocalTime;

public record TripStopResponseDTO(
        Long tripStopId,
        Integer pointId,
        String pointName,
        Short stopOrder,
        LocalTime scheduledTime,
        StopRoleEnum stopRole
) {}