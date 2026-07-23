package dev.kartikm09.orders.infrastructure;

import dev.kartikm09.orders.application.OrderRepository;
import dev.kartikm09.orders.domain.Order;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    @Override public void save(Order order) { orders.put(order.id(), order); }
    @Override public Optional<Order> find(String orderId) { return Optional.ofNullable(orders.get(orderId)); }
}
