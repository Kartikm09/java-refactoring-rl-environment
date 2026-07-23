package dev.kartikm09.orders.performance;

import dev.kartikm09.orders.domain.OrderLine;
import dev.kartikm09.orders.domain.PricingEngine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class PricingBenchmark {
    private final PricingEngine engine = new PricingEngine(List.of());
    private final List<OrderLine> lines = List.of(new OrderLine("A", 10, 99), new OrderLine("B", 5, 150));
    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public long calculatePrice() { return engine.price(lines); }
}
