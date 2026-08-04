package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.TripStatusEnum;

public record TripStatusUpdateRequestDTO(
        TripStatusEnum status
) {}