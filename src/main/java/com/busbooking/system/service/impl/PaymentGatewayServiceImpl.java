package com.busbooking.system.service.impl;

import com.busbooking.system.config.StripeConfig;
import com.busbooking.system.entity.Payment;
import com.busbooking.system.service.PaymentGatewayService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    private final StripeConfig stripeConfig;

    public PaymentGatewayServiceImpl(StripeConfig stripeConfig) {
        this.stripeConfig = stripeConfig;
    }

    @Override
    public String createCheckoutSession(Payment payment) {
        try {
            long amountInCents = payment.getAmount()
                    .multiply(new BigDecimal("100"))
                    .longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(stripeConfig.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(stripeConfig.getCancelUrl())
                    .setClientReferenceId(payment.getTransactionReference())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Bus Ticket Booking Reference: " + payment.getBooking().getBookingId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe Checkout Session", e);
        }
    }

    @Override
    public Event verifyWebhookSignature(String payload, String signatureHeader) {
        try {
            return Webhook.constructEvent(payload, signatureHeader, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            throw new SecurityException("Stripe webhook signature verification failed", e);
        }
    }
}
