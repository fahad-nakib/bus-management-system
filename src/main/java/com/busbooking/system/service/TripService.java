package com.busbooking.system.service;

import com.busbooking.system.dto.TripScheduleRequestDTO;
import com.busbooking.system.dto.TripStatusUpdateRequestDTO;
import com.busbooking.system.dto.TripResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TripService {
    TripResponseDTO scheduleTrip(TripScheduleRequestDTO request);
    TripResponseDTO getTripById(Long tripId);
    List<TripResponseDTO> searchTrips(Integer routeId, LocalDate journeyDate);
    TripResponseDTO updateTripStatus(Long tripId, TripStatusUpdateRequestDTO request);
}