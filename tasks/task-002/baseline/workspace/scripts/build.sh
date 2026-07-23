set -euo pipefail
rm -rf build
mkdir -p build/classes build/test-classes
find src -name '*.java' -print0 | sort -z | xargs -0 javac -Xlint:all -Werror -d build/classes
