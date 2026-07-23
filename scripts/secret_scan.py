from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATTERNS = {
    "private-key": re.compile(r"-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----"),
    "github-token": re.compile(r"gh[opusr]_[A-Za-z0-9]{20,}"),
    "generic-secret": re.compile(
        r"(?i)(?:api[_-]?key|password|secret)\s*[:=]\s*['\"][^'\"]{8,}"
    ),
}
IGNORED = {".git", ".gradle", "build", "reports"}

findings: list[str] = []
for path in ROOT.rglob("*"):
    if not path.is_file() or any(part in IGNORED for part in path.parts):
        continue
    try:
        text = path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        continue
    for name, pattern in PATTERNS.items():
        if pattern.search(text):
            findings.append(f"{path.relative_to(ROOT)}:{name}")

if findings:
    print("\n".join(findings), file=sys.stderr)
    raise SystemExit(1)

print("secret scan passed")
