package com.busbooking.system.repository;

import com.busbooking.system.entity.TripSeat;
import com.busbooking.system.entity.enums.SeatStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TripSeatRepository extends JpaRepository<TripSeat, Long> {

    @Query("SELECT ts FROM TripSeat ts LEFT JOIN FETCH ts.trip WHERE ts.tripSeatId IN :ids")
    List<TripSeat> findAllByIdWithTripFetch(@Param("ids") List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TripSeat ts SET ts.seatStatus = :status, ts.updatedAt = :now WHERE ts.tripSeatId IN :ids")
    int bulkUpdateSeatStatus(@Param("ids") List<Long> ids, @Param("status") SeatStatusEnum status, @Param("now") ZonedDateTime now);

    List<TripSeat> findByTripTripId(Long tripId);
}
