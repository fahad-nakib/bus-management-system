package com.busbooking.system.repository;

import com.busbooking.system.entity.TripSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripSeatRepository extends JpaRepository<TripSeat, Long> {
    List<TripSeat> findByTripTripId(Long tripId);
}