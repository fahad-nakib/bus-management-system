package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.PointTypeEnum;

public record BoardingDroppingPointResponse(
        Integer pointId,
        Integer routeStopId,
        String pointName,
        PointTypeEnum pointType,
        String address,
        String landmark,
        String contactNumber,
        Integer defaultOffsetMinutes,
        Boolean isActive
) {}