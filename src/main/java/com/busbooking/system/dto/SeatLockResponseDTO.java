package com.busbooking.system.dto;

import java.util.List;

public record SeatLockResponseDTO(
        List<Long> lockedSeatIds,
        String status,
        String message
) {}