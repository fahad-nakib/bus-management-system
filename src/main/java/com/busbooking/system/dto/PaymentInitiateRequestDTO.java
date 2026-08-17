package com.busbooking.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiateRequestDTO {
    @NotNull(message = "Booking ID cannot be null")
    private Long bookingId;
}
