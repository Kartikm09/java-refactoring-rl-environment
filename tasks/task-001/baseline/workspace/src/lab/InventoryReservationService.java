package lab;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InventoryReservationService {
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();
    private final Map<String, Reservation> completed = new ConcurrentHashMap<>();
    private final AuditLog audit;

    public InventoryReservationService(Map<String, Integer> initial, AuditLog audit) {
        stock.putAll(initial);
        this.audit = audit;
    }

    public Reservation reserve(String key, String sku, int quantity) {
        if (quantity <= 0) { throw new IllegalArgumentException("quantity must be positive"); }
        int available = stock.getOrDefault(sku, 0);
        if (available < quantity) { throw new IllegalStateException("insufficient stock"); }
        stock.put(sku, available - quantity);
        Reservation created = new Reservation(key, sku, quantity);
        Reservation previous = completed.putIfAbsent(key, created);
        if (previous != null) { return previous; }
        audit.append("reserved:" + key + ":" + sku + ":" + quantity);
        return created;
    }

    public int available(String sku) { return stock.getOrDefault(sku, 0); }
    public record Reservation(String key, String sku, int quantity) { }
}
