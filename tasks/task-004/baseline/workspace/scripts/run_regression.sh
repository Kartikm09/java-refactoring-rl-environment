set -euo pipefail
find tests/regression -name '*.java' -print0 | sort -z | xargs -0 javac -Xlint:all -Werror -cp build/classes -d build/test-classes
java -ea -cp build/classes:build/test-classes lab.RegressionTests
