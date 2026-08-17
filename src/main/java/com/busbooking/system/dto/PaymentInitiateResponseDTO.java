package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.PaymentStatusEnum;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiateResponseDTO {
    private Long paymentId;
    private String redirectUrl;
    private String transactionReference;
    private PaymentStatusEnum paymentStatus;
}
