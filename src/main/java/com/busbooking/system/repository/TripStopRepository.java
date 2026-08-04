package com.busbooking.system.repository;

import com.busbooking.system.entity.TripStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripStopRepository extends JpaRepository<TripStop, Long> {

    List<TripStop> findByTripTripIdOrderByStopOrderAsc(Long tripId);

    @Query("SELECT ts FROM TripStop ts " +
            "JOIN FETCH ts.boardingDroppingPoint " +
            "WHERE ts.trip.tripId IN :tripIds " +
            "ORDER BY ts.stopOrder ASC")
    List<TripStop> findByTripTripIdInWithPoint(@Param("tripIds") List<Long> tripIds);
}