package com.learning.kafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Payment Model - Represents a payment event in the order processing flow
 * <p>
 * Used in topics:
 * - payment-processed: When payment is successfully processed
 * - payment-failed: When payment fails
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String paymentId;
    private String orderId;
    private String correlationId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String failureReason;
    private String transactionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant processedAt;

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        PAYPAL,
        BANK_TRANSFER,
        CRYPTO
    }

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    /*
     * TODO: Generate paymentId and transactionId
     * TODO: Set initial status to PENDING
     * TODO: Set processedAt to current instant
     * */
    public static Payment create(String orderId, String correlationId,
                                 BigDecimal amount, PaymentMethod method) {

        return Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(orderId)
                .correlationId(correlationId)
                .amount(amount)
                .paymentMethod(method)
                .status(PaymentStatus.PENDING)
                .transactionId("TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8))
                .processedAt(Instant.now())
                .build();
    }

    /**
     * TODO: Return new Payment with status COMPLETED
     * TODO: Update processedAt timestamp
     */
    public Payment complete() {
        return Payment.builder()
                .paymentId(this.paymentId)
                .orderId(this.orderId)
                .correlationId(this.correlationId)
                .amount(this.amount)
                .paymentMethod(this.paymentMethod)
                .status(PaymentStatus.COMPLETED)
                .transactionId(this.transactionId)
                .processedAt(Instant.now())
                .build();
    }

    /**
     * TODO: Return new Payment with status FAILED
     * TODO: Set the failure reason
     * TODO: Update processedAt timestamp
     */
    public Payment fail(String reason) {
        return Payment.builder()
                .paymentId(this.paymentId)
                .orderId(this.orderId)
                .correlationId(this.correlationId)
                .amount(this.amount)
                .paymentMethod(this.paymentMethod)
                .status(PaymentStatus.FAILED)
                .transactionId(this.transactionId)
                .processedAt(Instant.now())
                .failureReason(reason)
                .build();
    }
}
