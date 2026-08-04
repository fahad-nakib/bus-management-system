package com.busbooking.system.controller;

import com.busbooking.system.dto.BusCreateRequest;
import com.busbooking.system.dto.BusUpdateRequest;
import com.busbooking.system.dto.BusResponse;
import com.busbooking.system.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @PostMapping
    public ResponseEntity<BusResponse> createBus(@RequestBody BusCreateRequest request) {
        return new ResponseEntity<>(busService.createBus(request), HttpStatus.CREATED);
    }

    @PutMapping("/{busId}")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable Integer busId,
            @RequestBody BusUpdateRequest request) {
        return ResponseEntity.ok(busService.updateBus(busId, request));
    }

    @GetMapping("/{busId}")
    public ResponseEntity<BusResponse> getBusById(@PathVariable Integer busId) {
        return ResponseEntity.ok(busService.getBusById(busId));
    }

    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @GetMapping("/active")
    public ResponseEntity<List<BusResponse>> getActiveBuses() {
        return ResponseEntity.ok(busService.getActiveBuses());
    }

    @DeleteMapping("/{busId}")
    public ResponseEntity<Void> deleteBus(@PathVariable Integer busId) {
        busService.deleteBus(busId);
        return ResponseEntity.noContent().build();
    }
}