package com.busbooking.system.service.impl;

import com.busbooking.system.entity.Booking;
import com.busbooking.system.entity.Ticket;
import com.busbooking.system.entity.enums.TicketStatusEnum;
import com.busbooking.system.repository.BookingRepository;
import com.busbooking.system.repository.TicketRepository;
import com.busbooking.system.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public void issueTicket(Long bookingId) {
        log.info("Starting internal asynchronous ticket issuance flow for Booking ID: {}", bookingId);

        // Idempotency rule check: Verify ticket has not already been created for this booking
        if (ticketRepository.findByBookingBookingId(bookingId).isPresent()) {
            log.warn("Ticket already issued for Booking ID: {}. Skipping execution loop.", bookingId);
            return;
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking baseline data not found for ID: " + bookingId));

        // Generate clean tracking properties
        String generatedTicketNumber = "TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Dynamic string payload generation intended for future QR code scanners
        String trackingQrPayload = String.format("TICKET:%s|BOOKING:%d|USER:%s",
                generatedTicketNumber, bookingId, "PASSENGER_DATA_STUB");

        Ticket ticket = Ticket.builder()
                .booking(booking)
                .ticketNumber(generatedTicketNumber)
                .ticketStatus(TicketStatusEnum.ISSUED)
                .issuedAt(ZonedDateTime.now())
                .qrCodeData(trackingQrPayload)
                .build();

        ticketRepository.save(ticket);
        log.info("Ticket successfully allocated and persistent to database table with clean state tracking number: {}", generatedTicketNumber);
    }
}
