package com.busbooking.system.entity;

import com.busbooking.system.entity.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payments") // [cite: 60]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId; // [cite: 60]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking; // [cite: 60]

    @Column(name = "payment_method", length = 30, nullable = false)
    private String paymentMethod; // [cite: 60]

    @Column(name = "payment_gateway", length = 50)
    private String paymentGateway; // [cite: 60]

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatusEnum paymentStatus; // [cite: 61, 64]

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount; // [cite: 61]

    @Column(name = "transaction_reference", length = 100)
    private String transactionReference; // [cite: 61]

    @Column(name = "gateway_response", columnDefinition = "JSONB")
    private String gatewayResponse; // [cite: 61]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_agent_id")
    private User receivedByAgent; // [cite: 61]

    @Column(name = "paid_at")
    private ZonedDateTime paidAt; // [cite: 61]

    @Column(name = "refunded_at")
    private ZonedDateTime refundedAt; // [cite: 61]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 62]
}