package lab;
import java.util.List;
import java.util.Map;
import report.AllocationReporter;
public final class PublicTests {
    private PublicTests() { }
    public static void main(String[] args) {
        var result = new AllocationReporter().aggregate(List.of(new AllocationReporter.Line("A", 2), new AllocationReporter.Line("B", 3), new AllocationReporter.Line("A", 4)));
        if (!result.totals().equals(Map.of("A", 6, "B", 3))) throw new AssertionError(result);
        System.out.println("public allocation tests passed");
    }
}
