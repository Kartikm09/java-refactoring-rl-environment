package lab;

import java.util.Map;

public final class RegressionTests {
    private RegressionTests() { }
    public static void main(String[] args) {
        InventoryReservationService service = new InventoryReservationService(Map.of("A", 5, "B", 5), new AuditLog());
        service.reserve("a", "A", 2);
        service.reserve("b", "B", 3);
        if (service.available("A") != 3 || service.available("B") != 2) {
            throw new AssertionError("distinct reservations regressed");
        }
        try { service.reserve("bad", "A", 0); throw new AssertionError("zero accepted"); }
        catch (IllegalArgumentException expected) { }
        System.out.println("regression tests passed");
    }
}
