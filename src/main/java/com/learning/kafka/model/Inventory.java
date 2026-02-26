package com.learning.kafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    private String orderId;
    private String correlationId;
    private String sku;
    private Integer quantity;
    private ReservationStatus status;
    private String failureReason;
    private String warehouseId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant reservedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant expiresAt;

    public enum ReservationStatus {
        PENDING,
        RESERVED,
        RELEASED,
        EXPIRED,
        FAILED
    }

    public static Inventory create(String orderId, String correlationId,
                                   String sku, Integer quantity, String warehouseId) {
        Instant now = Instant.now();

        return Inventory.builder()
                .orderId(orderId)
                .correlationId(correlationId)
                .sku(sku)
                .quantity(quantity)
                .warehouseId(warehouseId)
                .status(ReservationStatus.PENDING)
                .expiresAt(now.plusSeconds(900))
                .build();
    }

    public Inventory reserve() {
        Instant now = Instant.now();

        return Inventory.builder()
                .orderId(orderId)
                .correlationId(correlationId)
                .sku(sku)
                .quantity(quantity)
                .warehouseId(warehouseId)
                .status(ReservationStatus.RESERVED)
                .reservedAt(now)
                .build();
    }

    public Inventory release() {
        Instant now = Instant.now();

        return Inventory.builder()
                .orderId(orderId)
                .correlationId(correlationId)
                .sku(sku)
                .quantity(quantity)
                .warehouseId(warehouseId)
                .status(ReservationStatus.RELEASED)
                .build();
    }

    public Inventory fail(String reason) {
        Instant now = Instant.now();

        return Inventory.builder()
                .orderId(orderId)
                .correlationId(correlationId)
                .sku(sku)
                .quantity(quantity)
                .warehouseId(warehouseId)
                .status(ReservationStatus.FAILED)
                .failureReason(reason)
                .build();
    }
}
