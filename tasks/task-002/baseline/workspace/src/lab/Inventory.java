package lab;
import java.util.HashMap;
import java.util.Map;
public final class Inventory {
    private final Map<String, Integer> available = new HashMap<>();
    public Inventory(Map<String, Integer> stock) { available.putAll(stock); }
    public void release(String sku, int quantity) { available.merge(sku, quantity, Math::addExact); }
    public int available(String sku) { return available.getOrDefault(sku, 0); }
}
