package com.busbooking.system.controller;

import com.busbooking.system.dto.TripScheduleRequestDTO;
import com.busbooking.system.dto.TripStatusUpdateRequestDTO;
import com.busbooking.system.dto.TripResponseDTO;
import com.busbooking.system.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripResponseDTO> scheduleTrip(@RequestBody TripScheduleRequestDTO request) {
        return new ResponseEntity<>(tripService.scheduleTrip(request), HttpStatus.CREATED);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.getTripById(tripId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TripResponseDTO>> searchTrips(
            @RequestParam Integer routeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate) {
        return ResponseEntity.ok(tripService.searchTrips(routeId, journeyDate));
    }

    @PatchMapping("/{tripId}/status")
    public ResponseEntity<TripResponseDTO> updateTripStatus(
            @PathVariable Long tripId,
            @RequestBody TripStatusUpdateRequestDTO request) {
        return ResponseEntity.ok(tripService.updateTripStatus(tripId, request));
    }
}