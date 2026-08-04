package com.busbooking.system.service;

import com.busbooking.system.dto.RouteCreateRequest;
import com.busbooking.system.dto.RouteStopRequest;
import com.busbooking.system.dto.RouteUpdateRequest;
import com.busbooking.system.dto.RouteResponse;
import com.busbooking.system.dto.RouteStopResponse;

import java.util.List;

public interface RouteService {
    RouteResponse createRoute(RouteCreateRequest request);
    RouteResponse updateRoute(Integer routeId, RouteUpdateRequest request);
    RouteResponse getRouteById(Integer routeId);
    List<RouteResponse> getAllRoutes();
    RouteStopResponse addStopToExistingRoute(Integer routeId, RouteStopRequest newStopRequest, Integer insertAtIndex);
}