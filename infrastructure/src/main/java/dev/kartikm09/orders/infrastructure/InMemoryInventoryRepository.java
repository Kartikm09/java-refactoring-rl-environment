package dev.kartikm09.orders.infrastructure;

import dev.kartikm09.orders.application.InventoryRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryInventoryRepository implements InventoryRepository {
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();
    public InMemoryInventoryRepository(Map<String, Integer> initial) { stock.putAll(initial); }
    @Override public void reserve(String sku, int quantity) {
        if (quantity <= 0) { throw new IllegalArgumentException("quantity must be positive"); }
        stock.compute(sku, (key, current) -> {
            if (current == null || current < quantity) { throw new IllegalStateException("insufficient stock"); }
            return current - quantity;
        });
    }
    @Override public void release(String sku, int quantity) { stock.merge(sku, quantity, Math::addExact); }
    @Override public int available(String sku) { return stock.getOrDefault(sku, 0); }
}
