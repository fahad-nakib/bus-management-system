package com.busbooking.system.service.scheduler;

import com.busbooking.system.entity.SeatLock;
import com.busbooking.system.entity.enums.LockStatusEnum;
import com.busbooking.system.entity.enums.SeatStatusEnum;
import com.busbooking.system.repository.SeatLockRepository;
import com.busbooking.system.repository.TripSeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class SeatLockScheduler {

    private final SeatLockRepository seatLockRepository;
    private final TripSeatRepository tripSeatRepository;

    public SeatLockScheduler(SeatLockRepository seatLockRepository, TripSeatRepository tripSeatRepository) {
        this.seatLockRepository = seatLockRepository;
        this.tripSeatRepository = tripSeatRepository;
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void cleanupExpiredLocks() {
        ZonedDateTime now = ZonedDateTime.now();
        List<SeatLock> expiredLocks = seatLockRepository.findExpiredLocksWithTripSeats(LockStatusEnum.ACTIVE, now);

        if (expiredLocks.isEmpty()) {
            return;
        }

        List<Long> expiredLockIds = expiredLocks.stream()
                .map(SeatLock::getLockId)
                .toList();

        List<Long> tripSeatIdsToUnlock = expiredLocks.stream()
                .map(lock -> lock.getTripSeat().getTripSeatId())
                .distinct()
                .toList();

        seatLockRepository.bulkUpdateLockStatus(expiredLockIds, LockStatusEnum.EXPIRED, now);
        tripSeatRepository.bulkUpdateSeatStatus(tripSeatIdsToUnlock, SeatStatusEnum.AVAILABLE, now);
    }
}