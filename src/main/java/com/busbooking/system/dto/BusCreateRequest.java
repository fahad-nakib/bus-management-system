package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.AcTypeEnum;
import com.busbooking.system.entity.enums.ClassTypeEnum;

public record BusCreateRequest(
        String busNumber,
        String registrationNumber,
        Integer layoutId,
        AcTypeEnum acType,
        ClassTypeEnum classType,
        String manufacturer,
        Short modelYear,
        Boolean isActive
) {}