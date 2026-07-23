package lab;
import java.util.Map;
public final class RegressionTests {
    private RegressionTests() { }
    public static void main(String[] args) {
        Order order = new Order(Map.of("A", 2, "B", 1)); Inventory inventory = new Inventory(Map.of()); Audit audit = new Audit();
        order.cancelAll(inventory, audit);
        if (!order.remaining().isEmpty() || inventory.available("A") != 2 || inventory.available("B") != 1 || !audit.events().equals(java.util.List.of("ORDER_CANCELLED"))) throw new AssertionError();
        System.out.println("full cancellation regression passed");
    }
}
