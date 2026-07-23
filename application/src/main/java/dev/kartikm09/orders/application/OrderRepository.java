package dev.kartikm09.orders.application;

import dev.kartikm09.orders.domain.Order;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> find(String orderId);
}
