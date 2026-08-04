package com.busbooking.system.service.impl;

import com.busbooking.system.dto.TripScheduleRequestDTO;
import com.busbooking.system.dto.TripStatusUpdateRequestDTO;
import com.busbooking.system.dto.TripResponseDTO;
import com.busbooking.system.dto.TripStopResponseDTO;
import com.busbooking.system.entity.*;
import com.busbooking.system.entity.enums.SeatStatusEnum;
import com.busbooking.system.entity.enums.StopRoleEnum;
import com.busbooking.system.entity.enums.TripStatusEnum;
import com.busbooking.system.repository.*;
import com.busbooking.system.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final TripSeatRepository tripSeatRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final BoardingDroppingPointRepository pointRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public TripResponseDTO scheduleTrip(TripScheduleRequestDTO request) {
        if (tripRepository.existsByBusBusIdAndJourneyDateAndDepartureTime(
                request.busId(), request.journeyDate(), request.departureTime())) {
            throw new IllegalArgumentException("Bus is already scheduled for another trip at this date and time.");
        }

        Bus bus = busRepository.findById(request.busId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + request.busId()));

        Route route = routeRepository.findById(request.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + request.routeId()));

        User createdBy = userRepository.findById(request.createdByUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.createdByUserId()));

        if (request.offerPrice() != null && request.offerPrice().compareTo(request.regularPrice()) >= 0) {
            throw new IllegalArgumentException("Offer price must be lower than the regular price.");
        }

        LocalTime departureTime = request.departureTime();
        Integer durationMinutes = route.getEstimatedDurationMinutes();
        LocalTime arrivalTime = departureTime.plusMinutes(durationMinutes);

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setRoute(route);
        trip.setJourneyDate(request.journeyDate());
        trip.setDepartureTime(departureTime);
        trip.setArrivalTime(arrivalTime);
        trip.setDurationMinutes(durationMinutes);
        trip.setRegularPrice(request.regularPrice());
        trip.setOfferPrice(request.offerPrice());
        trip.setTotalSeats(bus.getTotalSeats());
        trip.setAvailableSeats(bus.getTotalSeats());
        trip.setTripStatus(TripStatusEnum.SCHEDULED);
        trip.setCreatedBy(createdBy);
        trip.setCreatedAt(ZonedDateTime.now());

        Trip savedTrip = tripRepository.save(trip);

        List<BoardingDroppingPoint> points = pointRepository.findByRouteStopRouteRouteId(route.getRouteId());
        List<TripStop> tripStops = new ArrayList<>();

        for (BoardingDroppingPoint point : points) {
            TripStop tripStop = new TripStop();
            tripStop.setTrip(savedTrip);
            tripStop.setBoardingDroppingPoint(point);
            tripStop.setStopOrder(point.getRouteStop().getStopOrder());

            LocalTime scheduledTime = departureTime.plusMinutes(point.getDefaultOffsetMinutes());
            tripStop.setScheduledTime(scheduledTime);

            StopRoleEnum stopRole = StopRoleEnum.valueOf(point.getPointType().name());
            tripStop.setStopRole(stopRole);

            tripStops.add(tripStop);
        }

        List<TripStop> savedTripStops = tripStopRepository.saveAll(tripStops);
        populateTripSeats(savedTrip, bus.getBusLayout().getLayoutId());

        return mapToTripResponse(savedTrip, savedTripStops);
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponseDTO getTripById(Long tripId) {
        Trip trip = tripRepository.findByIdWithDetails(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found with ID: " + tripId));

        List<TripStop> stops = tripStopRepository.findByTripTripIdOrderByStopOrderAsc(tripId);
        return mapToTripResponse(trip, stops);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponseDTO> searchTrips(Integer routeId, LocalDate journeyDate) {
        // Fetch all trips with Bus, Route, and User in 1 DB query
        List<Trip> trips = tripRepository.findByRouteRouteIdAndJourneyDateWithDetails(routeId, journeyDate);
        if (trips.isEmpty()) {
            return Collections.emptyList();
        }

        // Fetch all trip stops for all retrieved trips in 1 single DB query (Fixes N+1)
        List<Long> tripIds = trips.stream().map(Trip::getTripId).toList();
        List<TripStop> allStops = tripStopRepository.findByTripTripIdInWithPoint(tripIds);

        // Group stops by trip ID in-memory
        Map<Long, List<TripStop>> stopsByTripId = allStops.stream()
                .collect(Collectors.groupingBy(stop -> stop.getTrip().getTripId()));

        return trips.stream()
                .map(trip -> mapToTripResponse(
                        trip,
                        stopsByTripId.getOrDefault(trip.getTripId(), Collections.emptyList())
                ))
                .toList();
    }

    @Override
    @Transactional
    public TripResponseDTO updateTripStatus(Long tripId, TripStatusUpdateRequestDTO request) {
        Trip trip = tripRepository.findByIdWithDetails(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found with ID: " + tripId));

        trip.setTripStatus(request.status());
        trip.setUpdatedAt(ZonedDateTime.now());
        Trip updatedTrip = tripRepository.save(trip);

        List<TripStop> stops = tripStopRepository.findByTripTripIdOrderByStopOrderAsc(tripId);
        return mapToTripResponse(updatedTrip, stops);
    }

    private void populateTripSeats(Trip trip, Integer layoutId) {
        List<Seat> layoutSeats = seatRepository.findByBusLayoutLayoutIdAndIsActiveTrue(layoutId);
        List<TripSeat> tripSeats = new ArrayList<>();

        for (Seat seat : layoutSeats) {
            TripSeat tripSeat = new TripSeat();
            tripSeat.setTrip(trip);
            tripSeat.setSeat(seat);
            tripSeat.setSeatStatus(SeatStatusEnum.AVAILABLE);
            tripSeat.setVersion(0);
            tripSeat.setUpdatedAt(ZonedDateTime.now());
            tripSeats.add(tripSeat);
        }

        tripSeatRepository.saveAll(tripSeats);
    }

    private TripResponseDTO mapToTripResponse(Trip trip, List<TripStop> stops) {
        List<TripStopResponseDTO> stopResponses = stops.stream()
                .map(s -> new TripStopResponseDTO(
                        s.getTripStopId(),
                        s.getBoardingDroppingPoint() != null ? s.getBoardingDroppingPoint().getPointId() : null,
                        s.getBoardingDroppingPoint() != null ? s.getBoardingDroppingPoint().getPointName() : null,
                        s.getStopOrder(),
                        s.getScheduledTime(),
                        s.getStopRole()
                ))
                .toList();

        return new TripResponseDTO(
                trip.getTripId(),
                trip.getBus() != null ? trip.getBus().getBusId() : null,
                trip.getBus() != null ? trip.getBus().getBusNumber() : null,
                trip.getRoute() != null ? trip.getRoute().getRouteId() : null,
                trip.getRoute() != null ? trip.getRoute().getOriginCity() : null,
                trip.getRoute() != null ? trip.getRoute().getDestinationCity() : null,
                trip.getJourneyDate(),
                trip.getDepartureTime(),
                trip.getArrivalTime(),
                trip.getDurationMinutes(),
                trip.getRegularPrice(),
                trip.getOfferPrice(),
                trip.getTotalSeats(),
                trip.getAvailableSeats(),
                trip.getTripStatus(),
                trip.getCreatedBy() != null ? trip.getCreatedBy().getUserId() : null,
                trip.getCreatedAt(),
                stopResponses
        );
    }
}