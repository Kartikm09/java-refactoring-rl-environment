package lab;
import java.util.Map;
public final class PublicTests {
    private PublicTests() { }
    public static void main(String[] args) {
        Order order = new Order(Map.of("A", 5)); Inventory inventory = new Inventory(Map.of("A", 1)); Audit audit = new Audit();
        order.cancelPart("A", 2, inventory, audit);
        if (order.remaining().get("A") != 3 || inventory.available("A") != 3 || audit.events().size() != 1) throw new AssertionError();
        System.out.println("public partial cancellation tests passed");
    }
}
