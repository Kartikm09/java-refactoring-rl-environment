package dev.kartikm09.orders.application;

import static org.assertj.core.api.Assertions.assertThat;
import dev.kartikm09.orders.domain.OrderLine;
import dev.kartikm09.orders.infrastructure.InMemoryAuditRepository;
import dev.kartikm09.orders.infrastructure.InMemoryInventoryRepository;
import dev.kartikm09.orders.infrastructure.InMemoryOrderRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class OrderServiceTest {
    @Test
    void duplicateConcurrentRequestsReserveOnce() throws Exception {
        var inventory = new InMemoryInventoryRepository(Map.of("SKU", 10));
        var audit = new InMemoryAuditRepository();
        var service = new OrderService(inventory, new InMemoryOrderRepository(), audit, Clock.fixed(Instant.EPOCH, ZoneOffset.UTC));
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = new ArrayList<java.util.concurrent.Future<Void>>();
            for (int index = 0; index < 2; index++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    service.create("same-key", "order-1", List.of(new OrderLine("SKU", 3, 100)));
                    return null;
                }));
            }
            ready.await();
            start.countDown();
            for (var future : futures) { future.get(); }
        }
        assertThat(inventory.available("SKU")).isEqualTo(7);
        assertThat(audit.events()).hasSize(1);
    }

    @Test
    void failedMultiLineReservationRestoresEarlierStock() {
        var inventory = new InMemoryInventoryRepository(Map.of("AVAILABLE", 2, "EMPTY", 0));
        var service = new OrderService(
                inventory,
                new InMemoryOrderRepository(),
                new InMemoryAuditRepository(),
                Clock.fixed(Instant.EPOCH, ZoneOffset.UTC));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.create(
                        "key", "order-1", List.of(
                                new OrderLine("AVAILABLE", 1, 100),
                                new OrderLine("EMPTY", 1, 100))))
                .isInstanceOf(IllegalStateException.class);

        assertThat(inventory.available("AVAILABLE")).isEqualTo(2);
    }
}
