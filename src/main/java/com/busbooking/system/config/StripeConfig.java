package com.busbooking.system.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @PostConstruct
    public void initStripe() {
        // অ্যাপ্লিকেশন স্টার্ট হওয়ার সময় গ্লোবাল SDK ইনিশিয়ালাইজ হবে
        Stripe.apiKey = this.apiKey;
    }
}
