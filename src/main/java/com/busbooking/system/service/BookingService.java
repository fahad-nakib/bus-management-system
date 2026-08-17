package com.busbooking.system.service;

import com.busbooking.system.dto.BookingRequestDTO;
import com.busbooking.system.dto.BookingResponseDTO;

public interface BookingService {
    BookingResponseDTO processBookingConfirmation(BookingRequestDTO dto);
}