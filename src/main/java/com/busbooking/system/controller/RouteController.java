package com.busbooking.system.controller;

import com.busbooking.system.dto.RouteCreateRequest;
import com.busbooking.system.dto.RouteStopRequest;
import com.busbooking.system.dto.RouteUpdateRequest;
import com.busbooking.system.dto.RouteResponse;
import com.busbooking.system.dto.RouteStopResponse;
import com.busbooking.system.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(@RequestBody RouteCreateRequest request) {
        return new ResponseEntity<>(routeService.createRoute(request), HttpStatus.CREATED);
    }

    @PutMapping("/{routeId}")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable Integer routeId,
            @RequestBody RouteUpdateRequest request) {
        return ResponseEntity.ok(routeService.updateRoute(routeId, request));
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Integer routeId) {
        return ResponseEntity.ok(routeService.getRouteById(routeId));
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @PostMapping("/{routeId}/stops")
    public ResponseEntity<RouteStopResponse> addStopToExistingRoute(
            @PathVariable Integer routeId,
            @RequestBody RouteStopRequest stopRequest,
            @RequestParam Integer insertAtIndex) {
        return new ResponseEntity<>(
                routeService.addStopToExistingRoute(routeId, stopRequest, insertAtIndex),
                HttpStatus.CREATED
        );
    }
}