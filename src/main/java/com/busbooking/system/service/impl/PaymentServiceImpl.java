package com.busbooking.system.service.impl;

import com.busbooking.system.dto.PaymentCallbackDTO;
import com.busbooking.system.dto.PaymentInitiateRequestDTO;
import com.busbooking.system.dto.PaymentInitiateResponseDTO;
import com.busbooking.system.entity.Booking;
import com.busbooking.system.entity.Payment;
import com.busbooking.system.entity.enums.BookingStatusEnum;
import com.busbooking.system.entity.enums.PaymentMethodEnum;
import com.busbooking.system.entity.enums.PaymentStatusEnum;
import com.busbooking.system.event.PaymentCompletedEvent;
import com.busbooking.system.repository.BookingRepository;
import com.busbooking.system.repository.PaymentRepository;
import com.busbooking.system.service.PaymentGatewayService;
import com.busbooking.system.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final ApplicationEventPublisher eventPublisher;


    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentGatewayService paymentGatewayService,
                              ApplicationEventPublisher eventPublisher,
                              BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayService = paymentGatewayService;
        this.eventPublisher = eventPublisher;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public PaymentInitiateResponseDTO initiatePayment(PaymentInitiateRequestDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + dto.getBookingId()));

        String transactionReference = "TXN-" + UUID.randomUUID().toString();

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(PaymentMethodEnum.ONLINE_GATEWAY);
        payment.setPaymentGateway("STRIPE");
        payment.setPaymentStatus(PaymentStatusEnum.PENDING);
        // Assuming booking object has a getAmount() or totalFare field
        payment.setAmount(new java.math.BigDecimal("50.00"));
        payment.setTransactionReference(transactionReference);
        payment.setCreatedAt(ZonedDateTime.now());

        payment = paymentRepository.save(payment);

        String redirectUrl = paymentGatewayService.createCheckoutSession(payment);

        return PaymentInitiateResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .redirectUrl(redirectUrl)
                .transactionReference(transactionReference)
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }

    @Override
    @Transactional
    public void processGatewayCallback(PaymentCallbackDTO dto) {
        // Enforce pessimistic write lock to eliminate race conditions from duplicate webhooks
        Payment payment = paymentRepository.findByTransactionReferenceWithLock(dto.getTransactionReference())
                .orElseThrow(() -> new EntityNotFoundException("Payment reference not found: " + dto.getTransactionReference()));

        // Idempotency engine state validation
        if (payment.getPaymentStatus() == PaymentStatusEnum.SUCCESS || payment.getPaymentStatus() == PaymentStatusEnum.FAILED) {
            throw new IllegalStateException("Transaction has already been fully processed with status: " + payment.getPaymentStatus());
        }

        payment.setGatewayResponse(dto.getRawPayload());
        Booking booking = payment.getBooking();

        if ("SUCCESS".equalsIgnoreCase(dto.getGatewayStatus())) {
            payment.setPaymentStatus(PaymentStatusEnum.SUCCESS);
            payment.setPaidAt(ZonedDateTime.now());

            booking.setBookingStatus(BookingStatusEnum.CONFIRMED);

            paymentRepository.save(payment);

            // Dispatch domain transaction sync completion payload
            eventPublisher.publishEvent(new PaymentCompletedEvent(this, booking.getBookingId(), payment.getPaymentId()));
        } else {
            payment.setPaymentStatus(PaymentStatusEnum.FAILED);
            booking.setBookingStatus(BookingStatusEnum.FAILED);
            paymentRepository.save(payment);
        }
    }
}
