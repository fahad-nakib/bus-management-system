package com.busbooking.system.service.impl;

import com.busbooking.system.dto.BoardingDroppingPointRequest;
import com.busbooking.system.dto.RouteCreateRequest;
import com.busbooking.system.dto.RouteStopRequest;
import com.busbooking.system.dto.RouteUpdateRequest;
import com.busbooking.system.dto.BoardingDroppingPointResponse;
import com.busbooking.system.dto.RouteResponse;
import com.busbooking.system.dto.RouteStopResponse;
import com.busbooking.system.entity.BoardingDroppingPoint;
import com.busbooking.system.entity.Route;
import com.busbooking.system.entity.RouteStop;
import com.busbooking.system.entity.enums.PointTypeEnum;
import com.busbooking.system.repository.BoardingDroppingPointRepository;
import com.busbooking.system.repository.RouteRepository;
import com.busbooking.system.repository.RouteStopRepository;
import com.busbooking.system.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final BoardingDroppingPointRepository pointRepository;

    @Override
    @Transactional
    public RouteResponse createRoute(RouteCreateRequest request) {
        if (routeRepository.existsByOriginCityAndDestinationCity(request.originCity(), request.destinationCity())) {
            throw new IllegalArgumentException("Route already exists between " + request.originCity() + " and " + request.destinationCity());
        }

        Route route = new Route();
        route.setOriginCity(request.originCity());
        route.setDestinationCity(request.destinationCity());
        route.setDistanceKm(request.distanceKm());
        route.setEstimatedDurationMinutes(request.estimatedDurationMinutes());
        route.setIsActive(request.isActive() != null ? request.isActive() : true);
        route.setCreatedAt(ZonedDateTime.now());

        Route savedRoute = routeRepository.save(route);
        List<RouteStopResponse> stopResponses = new ArrayList<>();

        if (request.stops() != null && !request.stops().isEmpty()) {
            validateStopSequence(request.stops());

            // Prepare stops for bulk save
            List<RouteStop> stopsToSave = new ArrayList<>();
            for (RouteStopRequest stopReq : request.stops()) {
                RouteStop stop = new RouteStop();
                stop.setRoute(savedRoute);
                stop.setStopName(stopReq.stopName());
                stop.setCity(stopReq.city());
                stop.setStopOrder(stopReq.stopOrder());
                stopsToSave.add(stop);
            }

            List<RouteStop> savedStops = routeStopRepository.saveAll(stopsToSave);

            // Prepare points for bulk save
            List<BoardingDroppingPoint> pointsToSave = new ArrayList<>();
            for (int i = 0; i < request.stops().size(); i++) {
                RouteStopRequest stopReq = request.stops().get(i);
                RouteStop savedStop = savedStops.get(i);

                if (stopReq.points() != null) {
                    for (BoardingDroppingPointRequest pReq : stopReq.points()) {
                        BoardingDroppingPoint point = new BoardingDroppingPoint();
                        point.setRouteStop(savedStop);
                        point.setPointName(pReq.pointName());
                        point.setPointType(pReq.pointType());
                        point.setAddress(pReq.address());
                        point.setLandmark(pReq.landmark());
                        point.setContactNumber(pReq.contactNumber());
                        point.setDefaultOffsetMinutes(pReq.defaultOffsetMinutes());
                        point.setIsActive(pReq.isActive() != null ? pReq.isActive() : true);
                        pointsToSave.add(point);
                    }
                }
            }

            List<BoardingDroppingPoint> savedPoints = pointRepository.saveAll(pointsToSave);

            // Group points by Stop ID for constructing response without queries
            Map<Integer, List<BoardingDroppingPoint>> pointsByStopId = savedPoints.stream()
                    .collect(Collectors.groupingBy(p -> p.getRouteStop().getRouteStopId()));

            for (RouteStop savedStop : savedStops) {
                List<BoardingDroppingPoint> stopPoints = pointsByStopId.getOrDefault(savedStop.getRouteStopId(), Collections.emptyList());
                List<BoardingDroppingPointResponse> pointResponses = stopPoints.stream()
                        .map(this::mapToPointResponse)
                        .toList();
                stopResponses.add(mapToStopResponse(savedStop, pointResponses));
            }

            validateTimeOffsetMath(savedRoute.getRouteId());
        }

        return mapToRouteResponse(savedRoute, stopResponses);
    }

    @Override
    @Transactional
    public RouteResponse updateRoute(Integer routeId, RouteUpdateRequest request) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));

        route.setOriginCity(request.originCity());
        route.setDestinationCity(request.destinationCity());
        route.setDistanceKm(request.distanceKm());
        route.setEstimatedDurationMinutes(request.estimatedDurationMinutes());
        if (request.isActive() != null) {
            route.setIsActive(request.isActive());
        }

        Route updatedRoute = routeRepository.save(route);
        return getRouteById(updatedRoute.getRouteId());
    }

    @Override
    @Transactional(readOnly = true)
    public RouteResponse getRouteById(Integer routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));

        List<RouteStop> stops = routeStopRepository.findByRouteRouteIdOrderByStopOrderAsc(routeId);

        if (stops.isEmpty()) {
            return mapToRouteResponse(route, Collections.emptyList());
        }

        // Fix N+1: Fetch points for all stops in a single query
        List<Integer> stopIds = stops.stream().map(RouteStop::getRouteStopId).toList();
        List<BoardingDroppingPoint> allPoints = pointRepository.findByRouteStopRouteStopIdIn(stopIds);

        // Group points in memory
        Map<Integer, List<BoardingDroppingPoint>> pointsByStopId = allPoints.stream()
                .collect(Collectors.groupingBy(p -> p.getRouteStop().getRouteStopId()));

        List<RouteStopResponse> stopResponses = stops.stream().map(stop -> {
            List<BoardingDroppingPoint> points = pointsByStopId.getOrDefault(stop.getRouteStopId(), Collections.emptyList());
            List<BoardingDroppingPointResponse> pointResponses = points.stream()
                    .map(this::mapToPointResponse)
                    .toList();
            return mapToStopResponse(stop, pointResponses);
        }).toList();

        return mapToRouteResponse(route, stopResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteResponse> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        if (routes.isEmpty()) {
            return Collections.emptyList();
        }

        List<RouteStop> allStops = routeStopRepository.findAll();
        List<BoardingDroppingPoint> allPoints = pointRepository.findAll();

        // Group stops by route ID
        Map<Integer, List<RouteStop>> stopsByRouteId = allStops.stream()
                .sorted(Comparator.comparing(RouteStop::getStopOrder))
                .collect(Collectors.groupingBy(stop -> stop.getRoute().getRouteId()));

        // Group points by stop ID
        Map<Integer, List<BoardingDroppingPoint>> pointsByStopId = allPoints.stream()
                .collect(Collectors.groupingBy(p -> p.getRouteStop().getRouteStopId()));

        return routes.stream().map(route -> {
            List<RouteStop> stops = stopsByRouteId.getOrDefault(route.getRouteId(), Collections.emptyList());
            List<RouteStopResponse> stopResponses = stops.stream().map(stop -> {
                List<BoardingDroppingPoint> points = pointsByStopId.getOrDefault(stop.getRouteStopId(), Collections.emptyList());
                List<BoardingDroppingPointResponse> pointResponses = points.stream()
                        .map(this::mapToPointResponse)
                        .toList();
                return mapToStopResponse(stop, pointResponses);
            }).toList();

            return mapToRouteResponse(route, stopResponses);
        }).toList();
    }

    @Override
    @Transactional
    public RouteStopResponse addStopToExistingRoute(Integer routeId, RouteStopRequest newStopRequest, Integer insertAtIndex) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));

        // Dynamic Re-ordering Logic: Shift subsequent stops forward
        routeStopRepository.incrementStopOrdersFrom(routeId, insertAtIndex.shortValue());

        RouteStop newStop = new RouteStop();
        newStop.setRoute(route);
        newStop.setStopName(newStopRequest.stopName());
        newStop.setCity(newStopRequest.city());
        newStop.setStopOrder(insertAtIndex.shortValue());

        RouteStop savedStop = routeStopRepository.save(newStop);
        List<BoardingDroppingPointResponse> pointResponses = savePointsForStop(savedStop, newStopRequest.points());

        // Validate time sequence across the route chain after insertion
        validateTimeOffsetMath(routeId);

        return mapToStopResponse(savedStop, pointResponses);
    }

    private void validateStopSequence(List<RouteStopRequest> stops) {
        List<RouteStopRequest> sortedStops = stops.stream()
                .sorted(Comparator.comparing(RouteStopRequest::stopOrder))
                .toList();

        if (sortedStops.get(0).stopOrder() != 0) {
            throw new IllegalArgumentException("Route stops sequence must start at index 0.");
        }

        for (int i = 0; i < sortedStops.size(); i++) {
            if (sortedStops.get(i).stopOrder() != i) {
                throw new IllegalArgumentException("Invalid stop order sequence. Expected index " + i + " but found " + sortedStops.get(i).stopOrder());
            }
        }
    }

    private void validateTimeOffsetMath(Integer routeId) {
        List<BoardingDroppingPoint> allPoints = pointRepository.findByRouteStopRouteRouteId(routeId);

        int maxBoardingOffset = allPoints.stream()
                .filter(p -> p.getPointType() == PointTypeEnum.BOARDING || p.getPointType() == PointTypeEnum.BOTH)
                .mapToInt(BoardingDroppingPoint::getDefaultOffsetMinutes)
                .max()
                .orElse(-1);

        if (maxBoardingOffset != -1) {
            boolean invalidDroppingPointExists = allPoints.stream()
                    .filter(p -> p.getPointType() == PointTypeEnum.DROPPING)
                    .anyMatch(p -> p.getDefaultOffsetMinutes() < maxBoardingOffset);

            if (invalidDroppingPointExists) {
                throw new IllegalArgumentException("A DROPPING point offset cannot be lower than any BOARDING point offset in the route chain.");
            }
        }
    }

    private List<BoardingDroppingPointResponse> savePointsForStop(RouteStop stop, List<BoardingDroppingPointRequest> pointRequests) {
        if (pointRequests == null) return Collections.emptyList();

        List<BoardingDroppingPoint> pointsToSave = new ArrayList<>();
        for (BoardingDroppingPointRequest pReq : pointRequests) {
            BoardingDroppingPoint point = new BoardingDroppingPoint();
            point.setRouteStop(stop);
            point.setPointName(pReq.pointName());
            point.setPointType(pReq.pointType());
            point.setAddress(pReq.address());
            point.setLandmark(pReq.landmark());
            point.setContactNumber(pReq.contactNumber());
            point.setDefaultOffsetMinutes(pReq.defaultOffsetMinutes());
            point.setIsActive(pReq.isActive() != null ? pReq.isActive() : true);
            pointsToSave.add(point);
        }

        List<BoardingDroppingPoint> savedPoints = pointRepository.saveAll(pointsToSave);
        return savedPoints.stream().map(this::mapToPointResponse).toList();
    }

    private RouteResponse mapToRouteResponse(Route route, List<RouteStopResponse> stops) {
        return new RouteResponse(
                route.getRouteId(),
                route.getOriginCity(),
                route.getDestinationCity(),
                route.getDistanceKm(),
                route.getEstimatedDurationMinutes(),
                route.getIsActive(),
                route.getCreatedAt(),
                stops
        );
    }

    private RouteStopResponse mapToStopResponse(RouteStop stop, List<BoardingDroppingPointResponse> points) {
        return new RouteStopResponse(
                stop.getRouteStopId(),
                stop.getRoute().getRouteId(),
                stop.getStopName(),
                stop.getCity(),
                stop.getStopOrder(),
                points
        );
    }

    private BoardingDroppingPointResponse mapToPointResponse(BoardingDroppingPoint point) {
        return new BoardingDroppingPointResponse(
                point.getPointId(),
                point.getRouteStop().getRouteStopId(),
                point.getPointName(),
                point.getPointType(),
                point.getAddress(),
                point.getLandmark(),
                point.getContactNumber(),
                point.getDefaultOffsetMinutes(),
                point.getIsActive()
        );
    }
}