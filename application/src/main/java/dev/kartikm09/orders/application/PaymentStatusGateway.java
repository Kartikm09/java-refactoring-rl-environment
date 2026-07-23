package dev.kartikm09.orders.application;

import dev.kartikm09.orders.domain.PaymentStatusEvent;

@FunctionalInterface
public interface PaymentStatusGateway {
    PaymentStatusEvent fetch(String orderId);
}
