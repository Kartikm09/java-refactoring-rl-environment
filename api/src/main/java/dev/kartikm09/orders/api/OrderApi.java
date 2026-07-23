package dev.kartikm09.orders.api;

import dev.kartikm09.orders.application.OrderService;
import dev.kartikm09.orders.domain.Order;
import dev.kartikm09.orders.domain.OrderLine;
import java.util.List;

public final class OrderApi {
    private final OrderService service;
    public OrderApi(OrderService service) { this.service = service; }
    public CreateOrderResponse create(CreateOrderRequest request) {
        Order order = service.create(request.idempotencyKey(), request.orderId(), request.lines());
        return new CreateOrderResponse(order.id(), order.status().name());
    }
    public record CreateOrderRequest(String idempotencyKey, String orderId, List<OrderLine> lines) {
        public CreateOrderRequest {
            lines = List.copyOf(lines);
        }
    }
    public record CreateOrderResponse(String orderId, String status) { }
}
