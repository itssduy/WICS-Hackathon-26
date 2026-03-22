package com.ireflect.controller;

import com.ireflect.document.UserDocument;
import com.ireflect.dto.ApiError;
import com.ireflect.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final UserRepository userRepository;

    public BillingController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(Authentication auth) {
        // Stripe integration placeholder
        // In production, this creates a Stripe Checkout session and returns the URL
        return ResponseEntity.ok(Map.of(
            "checkoutUrl", "https://checkout.stripe.com/placeholder"
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getBillingStatus(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDocument u = user.get();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("plan", u.getPlan().name());
        result.put("subscriptionStatus", u.getSubscriptionStatus().name());
        result.put("renewalDate", null); // Stripe-managed
        return ResponseEntity.ok(result);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> stripeWebhook(@RequestBody String payload,
                                            @RequestHeader(value = "Stripe-Signature", required = false) String signature) {
        // Stripe webhook verification and processing placeholder
        // In production: verify signature, parse event, update user plan/subscription
        return ResponseEntity.ok(Map.of("received", true));
    }
}
