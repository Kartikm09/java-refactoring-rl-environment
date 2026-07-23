# Architecture

The production baseline follows inward dependencies: `domain` has no project
dependencies; `application` owns use cases and ports; `infrastructure` implements
repositories; `api` maps stable request/response contracts; `app` composes and runs the
service. `test-fixtures` and `performance-tests` remain outside production paths.

Idempotency is enforced per key, inventory mutations are atomic per SKU, and cancellation
validates before compensation. The evaluator is a separate Python package and executes
only task-specific candidate copies.

See `DECISIONS.md` for the candidate visibility and reward-model decisions.
