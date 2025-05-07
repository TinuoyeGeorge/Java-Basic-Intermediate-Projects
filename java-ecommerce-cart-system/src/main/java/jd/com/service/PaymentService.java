package jd.com.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jd.com.model.Order;

public class PaymentService {
    private static final String STRIPE_API_KEY = "sk_test_your_test_key_here"; // Replace with your test key

    public PaymentService() {
        Stripe.apiKey = STRIPE_API_KEY;
    }

    public String processPayment(Order order) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (order.getTotal() * 100)) // Stripe uses cents
                .setCurrency("usd")
                .setDescription("Order #" + order.getId())
                .setPaymentMethod("pm_card_visa") // Test card for demo
                .setConfirm(true)
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getStatus(); // "succeeded" if successful
    }
}