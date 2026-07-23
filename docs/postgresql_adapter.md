# Optional PostgreSQL Adapter Design

A production extension would implement order, inventory, idempotency, and audit ports in
one database transaction using a unique idempotency key and row-level inventory update.
Testcontainers would then validate rollback and retry behavior against PostgreSQL. This
adapter is deliberately documented, not claimed as implemented in v0.1.0.
