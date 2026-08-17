package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.PaymentMethodEnum;
import com.busbooking.system.entity.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "payment_method", length = 30, nullable = false)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "payment_gateway", length = 50)
    private String paymentGateway;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatusEnum paymentStatus;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_reference", length = 100)
    private String transactionReference;

    @Column(name = "gateway_response", columnDefinition = "JSONB")
    private String gatewayResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_agent_id")
    private User receivedByAgent;

    @Column(name = "paid_at")
    private ZonedDateTime paidAt;

    @Column(name = "refunded_at")
    private ZonedDateTime refundedAt;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}