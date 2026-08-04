package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.AcTypeEnum;
import com.busbooking.system.entity.enums.ClassTypeEnum;
import java.time.ZonedDateTime;

public record BusResponse(
        Integer busId,
        String busNumber,
        String registrationNumber,
        Integer layoutId,
        String layoutName,
        AcTypeEnum acType,
        ClassTypeEnum classType,
        String manufacturer,
        Short modelYear,
        Short totalSeats,
        Boolean isActive,
        ZonedDateTime createdAt
) {}