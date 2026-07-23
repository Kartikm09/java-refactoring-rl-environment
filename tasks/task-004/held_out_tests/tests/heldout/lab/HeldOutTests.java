package lab;
import java.util.List;
import report.AllocationReporter;
public final class HeldOutTests {
    private HeldOutTests() { }
    public static void main(String[] args) {
        var reporter = new AllocationReporter();
        if (!reporter.aggregate(List.of()).totals().isEmpty()) throw new AssertionError();
        try { reporter.aggregate(List.of(new AllocationReporter.Line("A", 0))); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        System.out.println("held-out allocation tests passed");
    }
}
