package lab;

import java.util.Map;

public final class PublicTests {
    private PublicTests() { }
    public static void main(String[] args) {
        AuditLog audit = new AuditLog();
        InventoryReservationService service = new InventoryReservationService(Map.of("A", 10), audit);
        var first = service.reserve("key-1", "A", 3);
        var second = service.reserve("key-1", "A", 3);
        if (!first.equals(second) || service.available("A") != 7 || audit.events().size() != 1) {
            throw new AssertionError("duplicate reservation changed state");
        }
        System.out.println("public reservation tests passed");
    }
}
