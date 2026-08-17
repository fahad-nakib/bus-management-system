package com.busbooking.system.dto;

import java.math.BigDecimal;

public record BookingResponseDTO(
        String bookingReference,
        String status,
        BigDecimal totalAmount,
        String redirectUrl
) {}