package dev.kartikm09.orders.domain;

import java.time.Instant;

public record AuditEvent(String orderId, String type, String detail, Instant occurredAt) { }
