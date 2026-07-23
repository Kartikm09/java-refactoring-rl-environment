package lab;
import java.util.List;
import report.AllocationReporter;
public final class RegressionTests {
    private RegressionTests() { }
    public static void main(String[] args) {
        var result = new AllocationReporter().aggregate(List.of(new AllocationReporter.Line("A", 1), new AllocationReporter.Line("A", 2)));
        if (result.totals().get("A") != 3) throw new AssertionError();
        System.out.println("allocation regression passed");
    }
}
