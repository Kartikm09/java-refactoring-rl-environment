# Benchmark Evidence

Recorded on 2026-07-23 with Temurin 25.0.3 and Gradle 9.6.1 on an Apple M1 host.
These local measurements are reproducibility evidence, not cross-machine guarantees.

## Application Benchmark

Command: `./gradlew jmh`

```text
Benchmark                         Mode  Cnt   Score    Error   Units
PricingBenchmark.calculatePrice  thrpt    3  48.076 +/- 21.275  ops/us
```

JMH used two warm-up iterations, three measurement iterations, and one fork. The clean
Gradle build, Checkstyle, SpotBugs, tests, and JaCoCo tasks passed in the same run.

## Task 004 Before And After

The evaluator uses a deterministic operation metric because wall-clock thresholds are
unstable across CI hardware.

| Workspace | `allocation_units` | Result |
| --- | ---: | --- |
| Published baseline | 1,500 | Reproduced with `scripts/run_benchmark.sh` |
| Golden patch | 2 | Accepted; configured threshold is at most 12 |

The golden value is recorded in
`reports/evaluations/task-004/solution/result.json`. Functional, held-out, regression,
formatting, lint, build, and determinism gates also passed before the evaluator awarded
the benchmark score.
