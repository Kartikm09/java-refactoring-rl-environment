# Transaction And Failure Model

The synthetic in-memory service models a transaction boundary around order creation.
Requests sharing an idempotency key serialize on the same lock, completed requests are
replayed without another reservation, and a partial multi-line reservation is
compensated in reverse order if a later reservation or repository operation fails.

Payment status is represented by the `PaymentStatusGateway` port. Unit tests inject
small deterministic mocks for successful events, retryable transport failures, and
non-retryable contract failures. No payment network or customer data is used.

This compact model does not claim database-grade atomicity. A production adapter would
use a database transaction and durable outbox around inventory, order, and audit writes;
the optional PostgreSQL direction is documented separately.
