package com.busbooking.system.controller;

import com.busbooking.system.dto.BookingRequestDTO;
import com.busbooking.system.dto.BookingResponseDTO;
import com.busbooking.system.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> processBooking(@RequestBody BookingRequestDTO dto) {
        BookingResponseDTO response = bookingService.processBookingConfirmation(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}