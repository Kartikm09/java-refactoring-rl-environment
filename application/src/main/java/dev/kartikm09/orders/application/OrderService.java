package dev.kartikm09.orders.application;

import dev.kartikm09.orders.domain.AuditEvent;
import dev.kartikm09.orders.domain.Order;
import dev.kartikm09.orders.domain.OrderLine;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class OrderService {
    private final InventoryRepository inventory;
    private final OrderRepository orders;
    private final AuditRepository audit;
    private final Clock clock;
    private final Map<String, Object> idempotencyLocks = new ConcurrentHashMap<>();
    private final Map<String, Order> completed = new ConcurrentHashMap<>();

    public OrderService(InventoryRepository inventory, OrderRepository orders, AuditRepository audit, Clock clock) {
        this.inventory = Objects.requireNonNull(inventory);
        this.orders = Objects.requireNonNull(orders);
        this.audit = Objects.requireNonNull(audit);
        this.clock = Objects.requireNonNull(clock);
    }

    public Order create(String idempotencyKey, String orderId, List<OrderLine> lines) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        Object lock = idempotencyLocks.computeIfAbsent(idempotencyKey, ignored -> new Object());
        synchronized (lock) {
            Order existing = completed.get(idempotencyKey);
            if (existing != null) { return existing; }
            var reserved = new ArrayList<OrderLine>();
            try {
                for (OrderLine line : lines) {
                    inventory.reserve(line.sku(), line.quantity());
                    reserved.add(line);
                }
                Order order = new Order(orderId, lines);
                orders.save(order);
                audit.append(new AuditEvent(orderId, "ORDER_RESERVED", idempotencyKey, clock.instant()));
                completed.put(idempotencyKey, order);
                return order;
            } catch (RuntimeException failure) {
                for (int index = reserved.size() - 1; index >= 0; index--) {
                    OrderLine line = reserved.get(index);
                    inventory.release(line.sku(), line.quantity());
                }
                throw failure;
            }
        }
    }

    public void cancelPart(String orderId, String sku, int quantity) {
        Order order = orders.find(orderId).orElseThrow(() -> new IllegalArgumentException("unknown order"));
        synchronized (order) {
            order.cancel(sku, quantity);
            inventory.release(sku, quantity);
            audit.append(new AuditEvent(orderId, "ORDER_PARTIALLY_CANCELLED", sku + ":" + quantity, clock.instant()));
        }
    }
}
