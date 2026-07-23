package report;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AllocationReporter {
    public ReportResult aggregate(List<Line> lines) {
        Map<String, Integer> totals = new LinkedHashMap<>();
        int allocations = 0;
        for (Line line : lines) {
            if (line.quantity() <= 0) { throw new IllegalArgumentException("quantity must be positive"); }
            for (int index = 0; index < line.quantity(); index++) {
                totals.merge(line.sku(), 1, Math::addExact);
                allocations++;
            }
        }
        return new ReportResult(Map.copyOf(totals), allocations);
    }
    public record Line(String sku, int quantity) { }
    public record ReportResult(Map<String, Integer> totals, int allocationUnits) { }
}
