from __future__ import annotations
import json
import os
import subprocess
import sys
from pathlib import Path

root = Path(__file__).resolve().parents[1]
env = os.environ.copy()
env["PYTHONPATH"] = str(root / "evaluator/src")
cases = [("golden/solution.patch", "accepted"), ("incorrect_patches/malformed.patch", "malformed_patch"), ("incorrect_patches/prohibited-file.patch", "prohibited_file_change"), ("incorrect_patches/plausible-but-wrong.patch", None)]
for task in sorted((root / "tasks").glob("task-*")):
    for relative, expected in cases:
        label = Path(relative).stem
        output = root / "reports/evaluations" / task.name / label
        command = [sys.executable, "-m", "rl_evaluator.cli", "evaluate", "--repo-root", str(root), "--task", task.name, "--patch", str(task / relative), "--output", str(output)]
        completed = subprocess.run(command, cwd=root, env=env, check=False)
        result = json.loads((output / "result.json").read_text(encoding="utf-8"))
        if expected and result["classification"] != expected: raise SystemExit(f"{task.name}/{label}: expected {expected}, got {result['classification']}")
        if relative.startswith("incorrect_patches") and result["accepted"]: raise SystemExit(f"incorrect patch accepted: {task.name}/{label}")
print("accepted and rejected evaluator controls passed for all four tasks")
