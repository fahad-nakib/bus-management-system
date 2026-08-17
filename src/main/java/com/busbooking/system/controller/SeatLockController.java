package com.busbooking.system.controller;

import com.busbooking.system.dto.SeatLockRequestDTO;
import com.busbooking.system.dto.SeatLockResponseDTO;
import com.busbooking.system.service.SeatLockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/locks")
public class SeatLockController {

    private final SeatLockService seatLockService;

    public SeatLockController(SeatLockService seatLockService) {
        this.seatLockService = seatLockService;
    }

    @PostMapping
    public ResponseEntity<SeatLockResponseDTO> lockSeats(@RequestBody SeatLockRequestDTO dto) {
        SeatLockResponseDTO response = seatLockService.lockSeats(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}