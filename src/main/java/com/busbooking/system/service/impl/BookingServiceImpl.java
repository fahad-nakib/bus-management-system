package com.busbooking.system.service.impl;

import com.busbooking.system.dto.BookingRequestDTO;
import com.busbooking.system.dto.BookingResponseDTO;
import com.busbooking.system.dto.PassengerDTO;
import com.busbooking.system.dto.PaymentInitiateRequestDTO;
import com.busbooking.system.dto.PaymentInitiateResponseDTO;
import com.busbooking.system.entity.*;
import com.busbooking.system.entity.enums.BookingChannelEnum;
import com.busbooking.system.entity.enums.BookingStatusEnum;
import com.busbooking.system.entity.enums.LockStatusEnum;
import com.busbooking.system.entity.enums.SeatStatusEnum;
import com.busbooking.system.exceptions.LockExpiredException;
import com.busbooking.system.exceptions.SeatUnavailableException;
import com.busbooking.system.repository.*;
import com.busbooking.system.service.BookingService;
import com.busbooking.system.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatLockRepository seatLockRepository;
    private final TripSeatRepository tripSeatRepository;
    private final TripRepository tripRepository;
    private final BoardingDroppingPointRepository boardingDroppingPointRepository;
    private final UserRepository userRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final PaymentService paymentService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              SeatLockRepository seatLockRepository,
                              TripSeatRepository tripSeatRepository,
                              TripRepository tripRepository,
                              BoardingDroppingPointRepository boardingDroppingPointRepository,
                              UserRepository userRepository,
                              BookingPassengerRepository bookingPassengerRepository,
                              BookingSeatRepository bookingSeatRepository,
                              PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.seatLockRepository = seatLockRepository;
        this.tripSeatRepository = tripSeatRepository;
        this.tripRepository = tripRepository;
        this.boardingDroppingPointRepository = boardingDroppingPointRepository;
        this.userRepository = userRepository;
        this.bookingPassengerRepository = bookingPassengerRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public BookingResponseDTO processBookingConfirmation(BookingRequestDTO dto) {
        ZonedDateTime now = ZonedDateTime.now();

        List<SeatLock> activeLocks = seatLockRepository.findValidActiveLocks(
                dto.sessionId(), dto.tripSeatIds(), LockStatusEnum.ACTIVE, now
        );

        if (activeLocks.size() != dto.tripSeatIds().size()) {
            throw new LockExpiredException("One or more seat locks have expired or belong to an invalid session.");
        }

        Trip trip = tripRepository.findById(dto.tripId())
                .orElseThrow(() -> new IllegalArgumentException("Target trip not found."));

        List<TripSeat> tripSeats = tripSeatRepository.findAllByIdWithTripFetch(dto.tripSeatIds());

        BoardingDroppingPoint boarding = boardingDroppingPointRepository.getReferenceById(dto.boardingPointId());
        BoardingDroppingPoint dropping = boardingDroppingPointRepository.getReferenceById(dto.droppingPointId());
        User user = dto.userId() != null ? userRepository.getReferenceById(dto.userId()) : null;

        String bookingRef = "BK-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (TripSeat ts : tripSeats) {
            BigDecimal effectivePrice = ts.getPriceOverride() != null ? ts.getPriceOverride() : trip.getRegularPrice();
            totalAmount = totalAmount.add(effectivePrice);
        }

        Booking booking = new Booking();
        booking.setBookingReference(bookingRef);
        booking.setTrip(trip);
        booking.setUser(user);
        booking.setBoardingPoint(boarding);
        booking.setDroppingPoint(dropping);
        booking.setBookingChannel(BookingChannelEnum.ONLINE);
        booking.setBookingStatus(BookingStatusEnum.PENDING);
        booking.setTotalAmount(totalAmount);
        booking.setContactPhone(dto.contactPhone());
        booking.setContactEmail(dto.contactEmail());
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        Booking savedBooking = bookingRepository.save(booking);

        // --- N+1 FIX #1: Passengers batch insertion ---
        List<BookingPassenger> passengersToSave = new ArrayList<>();
        if (dto.passengers() != null) {
            for (PassengerDTO pDto : dto.passengers()) {
                BookingPassenger passenger = new BookingPassenger();
                passenger.setBooking(savedBooking);
                passenger.setFullName(pDto.name());
                passenger.setGender(pDto.gender());
                passenger.setAge(pDto.age());
                passenger.setPhoneNumber(pDto.contactPhone());
                passengersToSave.add(passenger);
            }
        }

        // লুপের বাইরে একবারেই সব সেভ করা হচ্ছে
        List<BookingPassenger> savedPassengers = passengersToSave.isEmpty()
                ? List.of()
                : bookingPassengerRepository.saveAll(passengersToSave);

        // --- N+1 FIX #2: Booking Seats batch insertion ---
        List<BookingSeat> bookingSeatsToSave = new ArrayList<>();
        for (int i = 0; i < tripSeats.size(); i++) {
            TripSeat ts = tripSeats.get(i);
            BigDecimal effectivePrice = ts.getPriceOverride() != null ? ts.getPriceOverride() : trip.getRegularPrice();

            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(savedBooking);
            bookingSeat.setTripSeat(ts);
            bookingSeat.setSeatPrice(effectivePrice);

            if (i < savedPassengers.size()) {
                bookingSeat.setPassenger(savedPassengers.get(i));
            }

            bookingSeatsToSave.add(bookingSeat);
        }

        // লুপের বাইরে একবারেই সব সেভ করা হচ্ছে
        bookingSeatRepository.saveAll(bookingSeatsToSave);

        // Bulk operations
        tripSeatRepository.bulkUpdateSeatStatus(dto.tripSeatIds(), SeatStatusEnum.BOOKED, now);

        List<Long> lockIds = activeLocks.stream().map(SeatLock::getLockId).toList();
        seatLockRepository.bulkUpdateLockStatus(lockIds, LockStatusEnum.CONFIRMED, now);

        int updatedTrips = tripRepository.decrementAvailableSeats(dto.tripId(), (short) dto.tripSeatIds().size(), now);
        if (updatedTrips == 0) {
            throw new SeatUnavailableException("Insufficient inventory available to decrement trip capacity.");
        }

        PaymentInitiateRequestDTO paymentParams = new PaymentInitiateRequestDTO(savedBooking.getBookingId());
        PaymentInitiateResponseDTO paymentInfo = paymentService.initiatePayment(paymentParams);

        return new BookingResponseDTO(bookingRef, "PENDING", totalAmount, paymentInfo.getRedirectUrl());
    }
}