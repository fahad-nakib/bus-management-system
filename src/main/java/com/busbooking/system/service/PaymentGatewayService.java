package com.busbooking.system.service;

import com.busbooking.system.entity.Payment;
import com.stripe.model.Event;

public interface PaymentGatewayService {
    String createCheckoutSession(Payment payment);
    Event verifyWebhookSignature(String payload, String signatureHeader);
}
