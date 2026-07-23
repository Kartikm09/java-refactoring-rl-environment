from __future__ import annotations
import json
from pathlib import Path

root = Path(__file__).resolve().parents[1]
required = ["README.md", "LICENSE", "CONTRIBUTING.md", "SECURITY.md", "Dockerfile", "docker-compose.yml", "evaluator"]
missing = [path for path in required if not (root / path).exists()]
tasks = sorted((root / "tasks").glob("task-*"))
if len(tasks) != 4: raise SystemExit(f"expected exactly four tasks, found {len(tasks)}")
for task in tasks:
    for path in ["task.yaml", "evaluator_config.json", "golden/solution.patch", "public_tests", "held_out_tests"]:
        if not (task / path).exists(): missing.append(str((task / path).relative_to(root)))
    json.loads((task / "evaluator_config.json").read_text(encoding="utf-8"))
if missing: raise SystemExit("missing required paths: " + ", ".join(missing))
print("repository structure validation passed")
