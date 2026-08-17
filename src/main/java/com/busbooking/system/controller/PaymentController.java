package com.busbooking.system.controller;

import com.busbooking.system.dto.PaymentCallbackDTO;
import com.busbooking.system.dto.PaymentInitiateRequestDTO;
import com.busbooking.system.dto.PaymentInitiateResponseDTO;
import com.busbooking.system.service.PaymentGatewayService;
import com.busbooking.system.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentGatewayService paymentGatewayService;

    public PaymentController(PaymentService paymentService, PaymentGatewayService paymentGatewayService) {
        this.paymentService = paymentService;
        this.paymentGatewayService = paymentGatewayService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentInitiateResponseDTO> initiatePayment(@Valid @RequestBody PaymentInitiateRequestDTO dto) {
        PaymentInitiateResponseDTO response = paymentService.initiatePayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        // Strict boundary structural verification check via signature parsing
        Event event;
        try {
            event = paymentGatewayService.verifyWebhookSignature(payload, sigHeader);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature structural boundary configuration");
        }

        // Route asynchronous events securely into the internal service transactional context
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject()
                    .orElseThrow(() -> new IllegalArgumentException("Failed to deserialize Stripe webhook payload session target object"));

            PaymentCallbackDTO callbackDto = PaymentCallbackDTO.builder()
                    .transactionReference(session.getClientReferenceId())
                    .gatewayStatus("SUCCESS")
                    .rawPayload(payload)
                    .build();

            paymentService.processGatewayCallback(callbackDto);
        }

        return ResponseEntity.ok("Webhook event executed successfully");
    }
}
