package lab;
import java.util.List;
import report.AllocationReporter;
public final class BenchmarkProbe {
    private BenchmarkProbe() { }
    public static void main(String[] args) {
        var result = new AllocationReporter().aggregate(List.of(new AllocationReporter.Line("A", 1000), new AllocationReporter.Line("B", 500)));
        System.out.printf("{\"metric\":\"allocation_units\",\"value\":%d}%n", result.allocationUnits());
    }
}
