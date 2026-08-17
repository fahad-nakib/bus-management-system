package com.busbooking.system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCallbackDTO {
    private String transactionReference;
    private String gatewayStatus;
    private String rawPayload;
}
