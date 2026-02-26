package com.learning.kafka.service;

import com.learning.kafka.exception.NonRetryableException;
import com.learning.kafka.exception.RetryableException;
import com.learning.kafka.model.Order;
import com.learning.kafka.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class PaymentService {

    private final Random random = new Random();

    public Payment processPayment(Order order) {
        log.info("Processing payment for order: {}", order.getOrderId());

        Payment payment = Payment.create(order.getOrderId(),
                order.getCorrelationId(),
                order.getTotalAmount(),
                Payment.PaymentMethod.CREDIT_CARD);

        int outcome = random.nextInt(100);

        if (outcome < 70) {
            log.info("Payment successful: {}", payment.getPaymentId());
            return payment.complete();
        } else if (outcome < 90) {
            log.warn("Temporary payment failure - will retry: {}", payment.getPaymentId());
            throw new RetryableException("Payment gateway temporarily unavailable");
        } else {
            log.error("Permanent payment failure: {}", payment.getPaymentId());
            throw new NonRetryableException("Invalid payment method");
        }
    }

    public Payment handlePaymentFailure(Order order, String reason) {
        Payment payment = Payment.create(
                order.getOrderId(),
                order.getCorrelationId(),
                order.getTotalAmount(),
                Payment.PaymentMethod.CREDIT_CARD
        );
        return payment.fail(reason);
    }
}
