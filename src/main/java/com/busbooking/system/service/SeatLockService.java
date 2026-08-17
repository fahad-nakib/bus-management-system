package com.busbooking.system.service;

import com.busbooking.system.dto.SeatLockRequestDTO;
import com.busbooking.system.dto.SeatLockResponseDTO;

public interface SeatLockService {
    SeatLockResponseDTO lockSeats(SeatLockRequestDTO dto);
}