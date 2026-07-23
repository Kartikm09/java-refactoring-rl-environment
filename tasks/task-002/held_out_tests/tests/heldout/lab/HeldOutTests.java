package lab;
import java.util.Map;
public final class HeldOutTests {
    private HeldOutTests() { }
    public static void main(String[] args) {
        Order order = new Order(Map.of("A", 2)); Inventory inventory = new Inventory(Map.of("A", 0)); Audit audit = new Audit();
        try { order.cancelPart("A", 3, inventory, audit); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        if (order.remaining().get("A") != 2 || inventory.available("A") != 0 || !audit.events().isEmpty()) throw new AssertionError("invalid request had side effects");
        order.cancelPart("A", 2, inventory, audit);
        if (!order.remaining().isEmpty() || !audit.events().get(0).equals("ORDER_PARTIALLY_CANCELLED:A:2")) throw new AssertionError();
        System.out.println("held-out partial cancellation tests passed");
    }
}
