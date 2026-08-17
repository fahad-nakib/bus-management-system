package com.busbooking.system.service.impl;

import com.busbooking.system.dto.SeatLockRequestDTO;
import com.busbooking.system.dto.SeatLockResponseDTO;
import com.busbooking.system.entity.SeatLock;
import com.busbooking.system.entity.TripSeat;
import com.busbooking.system.entity.User;
import com.busbooking.system.entity.enums.LockStatusEnum;
import com.busbooking.system.entity.enums.SeatStatusEnum;
import com.busbooking.system.exceptions.SeatUnavailableException;
import com.busbooking.system.repository.SeatLockRepository;
import com.busbooking.system.repository.TripSeatRepository;
import com.busbooking.system.repository.UserRepository;
import com.busbooking.system.service.SeatLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeatLockServiceImpl implements SeatLockService {

    private final TripSeatRepository tripSeatRepository;
    private final SeatLockRepository seatLockRepository;
    private final UserRepository userRepository;

    public SeatLockServiceImpl(TripSeatRepository tripSeatRepository,
                               SeatLockRepository seatLockRepository,
                               UserRepository userRepository) {
        this.tripSeatRepository = tripSeatRepository;
        this.seatLockRepository = seatLockRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public SeatLockResponseDTO lockSeats(SeatLockRequestDTO dto) {
        List<TripSeat> tripSeats = tripSeatRepository.findAllByIdWithTripFetch(dto.tripSeatIds());

        if (tripSeats.size() != dto.tripSeatIds().size()) {
            throw new SeatUnavailableException("One or more requested trip seats do not exist.");
        }

        for (TripSeat seat : tripSeats) {
            if (seat.getSeatStatus() != SeatStatusEnum.AVAILABLE) {
                throw new SeatUnavailableException("Seat ID " + seat.getTripSeatId() + " is no longer AVAILABLE.");
            }
        }

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime lockedUntil = now.plusMinutes(10);

        tripSeatRepository.bulkUpdateSeatStatus(dto.tripSeatIds(), SeatStatusEnum.LOCKED, now);

        User userRef = dto.userId() != null ? userRepository.getReferenceById(dto.userId()) : null;

        List<SeatLock> lockRecords = new ArrayList<>();
        for (TripSeat seat : tripSeats) {
            SeatLock lock = new SeatLock();
            lock.setTripSeat(seat);
            lock.setUser(userRef);
            lock.setSessionId(dto.sessionId());
            lock.setLockedAt(now);
            lock.setLockedUntil(lockedUntil);
            lock.setLockStatus(LockStatusEnum.ACTIVE);
            lockRecords.add(lock);
        }

        seatLockRepository.saveAll(lockRecords);

        return new SeatLockResponseDTO(
                dto.tripSeatIds(),
                "SUCCESS",
                "Seats successfully locked for 10 minutes."
        );
    }
}