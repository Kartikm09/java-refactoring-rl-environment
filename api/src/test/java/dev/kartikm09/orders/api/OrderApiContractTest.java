package dev.kartikm09.orders.api;

import static org.assertj.core.api.Assertions.assertThat;
import dev.kartikm09.orders.application.OrderService;
import dev.kartikm09.orders.domain.OrderLine;
import dev.kartikm09.orders.infrastructure.InMemoryAuditRepository;
import dev.kartikm09.orders.infrastructure.InMemoryInventoryRepository;
import dev.kartikm09.orders.infrastructure.InMemoryOrderRepository;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OrderApiContractTest {
    @Test
    void returnsStableReservedStatus() {
        var service = new OrderService(new InMemoryInventoryRepository(Map.of("SKU", 2)), new InMemoryOrderRepository(), new InMemoryAuditRepository(), Clock.systemUTC());
        var response = new OrderApi(service).create(new OrderApi.CreateOrderRequest("key", "order", List.of(new OrderLine("SKU", 1, 50))));
        assertThat(response.orderId()).isEqualTo("order");
        assertThat(response.status()).isEqualTo("RESERVED");
    }
}
