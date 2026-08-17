package com.busbooking.system.listener;

import com.busbooking.system.event.PaymentCompletedEvent;
import com.busbooking.system.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);
    private final TicketService ticketService;

    // Constructor injection secures decoupling from HTTP servlet worker allocations
    public PaymentEventListener(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Async
    @EventListener
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("System Payment Verification Interceptor Caught Event. Target Routing -> Booking ID: {}", event.getBookingId());
        try {
            ticketService.issueTicket(event.getBookingId());
        } catch (Exception e) {
            log.error("Fatal system infrastructure fail during asynchronous ticket processing for Booking ID: {}", event.getBookingId(), e);
            // Implement fallback strategies or dead letter queues (DLQ) if necessary
        }
    }
}
