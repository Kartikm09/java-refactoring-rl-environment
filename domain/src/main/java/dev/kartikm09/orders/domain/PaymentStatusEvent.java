package dev.kartikm09.orders.domain;

import java.time.Instant;
import java.util.Objects;

public record PaymentStatusEvent(String orderId, PaymentStatus status, Instant occurredAt) {
    public PaymentStatusEvent {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(occurredAt, "occurredAt");
    }
}
