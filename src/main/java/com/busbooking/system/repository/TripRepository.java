package com.busbooking.system.repository;

import com.busbooking.system.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT DISTINCT t FROM Trip t " +
            "JOIN FETCH t.bus " +
            "JOIN FETCH t.route " +
            "JOIN FETCH t.createdBy " +
            "WHERE t.route.routeId = :routeId AND t.journeyDate = :journeyDate")
    List<Trip> findByRouteRouteIdAndJourneyDateWithDetails(
            @Param("routeId") Integer routeId,
            @Param("journeyDate") LocalDate journeyDate
    );

    @Query("SELECT t FROM Trip t " +
            "JOIN FETCH t.bus " +
            "JOIN FETCH t.route " +
            "JOIN FETCH t.createdBy " +
            "WHERE t.tripId = :tripId")
    Optional<Trip> findByIdWithDetails(@Param("tripId") Long tripId);

    boolean existsByBusBusIdAndJourneyDateAndDepartureTime(Integer busId, LocalDate journeyDate, LocalTime departureTime);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Trip t SET t.availableSeats = t.availableSeats - :count, t.updatedAt = :now WHERE t.tripId = :tripId AND t.availableSeats >= :count")
    int decrementAvailableSeats(@Param("tripId") Long tripId, @Param("count") short count, @Param("now") ZonedDateTime now);
}