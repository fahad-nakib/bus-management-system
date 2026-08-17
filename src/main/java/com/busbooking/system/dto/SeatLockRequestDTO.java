package com.busbooking.system.dto;

import java.util.List;

public record SeatLockRequestDTO(
        List<Long> tripSeatIds,
        Long userId,
        String sessionId
) {}