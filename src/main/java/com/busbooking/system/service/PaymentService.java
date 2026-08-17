package com.busbooking.system.service;

import com.busbooking.system.dto.PaymentCallbackDTO;
import com.busbooking.system.dto.PaymentInitiateRequestDTO;
import com.busbooking.system.dto.PaymentInitiateResponseDTO;

public interface PaymentService {
    PaymentInitiateResponseDTO initiatePayment(PaymentInitiateRequestDTO dto);
    void processGatewayCallback(PaymentCallbackDTO dto);
}
