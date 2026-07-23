package lab;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Order {
    private final Map<String, Integer> remaining = new LinkedHashMap<>();
    public Order(Map<String, Integer> lines) { remaining.putAll(lines); }
    public Map<String, Integer> remaining() { return Map.copyOf(remaining); }
    public void cancelAll(Inventory inventory, Audit audit) {
        remaining.forEach(inventory::release);
        remaining.clear();
        audit.append("ORDER_CANCELLED");
    }
}
