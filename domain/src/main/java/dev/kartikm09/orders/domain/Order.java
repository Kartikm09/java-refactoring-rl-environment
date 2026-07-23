package dev.kartikm09.orders.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Order {
    private final String id;
    private final Map<String, Integer> remaining;
    private OrderStatus status;

    public Order(String id, List<OrderLine> lines) {
        this.id = Objects.requireNonNull(id, "id");
        if (id.isBlank() || lines.isEmpty()) {
            throw new IllegalArgumentException("order requires id and lines");
        }
        this.remaining = new LinkedHashMap<>();
        for (OrderLine line : lines) {
            remaining.merge(line.sku(), line.quantity(), Math::addExact);
        }
        this.status = OrderStatus.RESERVED;
    }

    public String id() { return id; }
    public synchronized OrderStatus status() { return status; }
    public synchronized Map<String, Integer> remainingQuantities() { return Map.copyOf(remaining); }

    public synchronized void cancel(String sku, int quantity) {
        if (quantity <= 0) { throw new IllegalArgumentException("quantity must be positive"); }
        Integer current = remaining.get(sku);
        if (current == null || quantity > current) { throw new IllegalArgumentException("invalid cancellation"); }
        int next = current - quantity;
        if (next == 0) {
            remaining.remove(sku);
        } else {
            remaining.put(sku, next);
        }
        status = remaining.isEmpty() ? OrderStatus.CANCELLED : OrderStatus.PARTIALLY_CANCELLED;
    }
}
