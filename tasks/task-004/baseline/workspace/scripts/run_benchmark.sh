set -euo pipefail
java -ea -cp build/classes:build/test-classes lab.BenchmarkProbe > benchmark_result.json
cat benchmark_result.json
