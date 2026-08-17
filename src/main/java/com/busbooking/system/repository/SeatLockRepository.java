package com.busbooking.system.repository;

import com.busbooking.system.entity.SeatLock;
import com.busbooking.system.entity.enums.LockStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface SeatLockRepository extends JpaRepository<SeatLock, Long> {

    @Query("SELECT sl FROM SeatLock sl JOIN FETCH sl.tripSeat WHERE sl.lockStatus = :status AND sl.lockedUntil < :now")
    List<SeatLock> findExpiredLocksWithTripSeats(@Param("status") LockStatusEnum status, @Param("now") ZonedDateTime now);

    @Query("SELECT sl FROM SeatLock sl JOIN FETCH sl.tripSeat WHERE sl.sessionId = :sessionId AND sl.tripSeat.tripSeatId IN :seatIds AND sl.lockStatus = :status AND sl.lockedUntil > :now")
    List<SeatLock> findValidActiveLocks(@Param("sessionId") String sessionId, @Param("seatIds") List<Long> seatIds, @Param("status") LockStatusEnum status, @Param("now") ZonedDateTime now);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SeatLock sl SET sl.lockStatus = :targetStatus, sl.releasedAt = :now WHERE sl.lockId IN :lockIds")
    int bulkUpdateLockStatus(@Param("lockIds") List<Long> lockIds, @Param("targetStatus") LockStatusEnum targetStatus, @Param("now") ZonedDateTime now);
}