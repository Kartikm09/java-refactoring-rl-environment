package lab;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public final class HeldOutTests {
    private HeldOutTests() { }
    public static void main(String[] args) throws Exception {
        AuditLog audit = new AuditLog();
        InventoryReservationService service = new InventoryReservationService(Map.of("A", 20), audit);
        CountDownLatch ready = new CountDownLatch(8);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int index = 0; index < 8; index++) {
                executor.submit(() -> { ready.countDown(); start.await(); service.reserve("retry", "A", 4); return null; });
            }
            ready.await(); start.countDown();
        }
        if (service.available("A") != 16 || audit.events().size() != 1) {
            throw new AssertionError("concurrent retry was not exactly once");
        }
        System.out.println("held-out concurrency tests passed");
    }
}
