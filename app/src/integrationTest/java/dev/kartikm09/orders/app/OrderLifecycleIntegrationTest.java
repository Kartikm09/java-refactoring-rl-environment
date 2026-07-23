package dev.kartikm09.orders.app;

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

class OrderLifecycleIntegrationTest {
    @Test
    void cancellationRestoresOnlyRequestedInventory() {
        var inventory = new InMemoryInventoryRepository(Map.of("SKU", 5));
        var orders = new InMemoryOrderRepository();
        var service = new OrderService(inventory, orders, new InMemoryAuditRepository(), Clock.systemUTC());
        service.create("key", "order", List.of(new OrderLine("SKU", 4, 100)));
        service.cancelPart("order", "SKU", 2);
        assertThat(inventory.available("SKU")).isEqualTo(3);
        assertThat(orders.find("order").orElseThrow().remainingQuantities()).containsEntry("SKU", 2);
    }
}
