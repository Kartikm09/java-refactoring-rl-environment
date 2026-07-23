# Acceptance Criteria

- A repeated idempotency key returns the original reservation without another stock decrement.
- Concurrent calls for one key produce one reservation and one audit event.
- Distinct keys may reserve independently.
- Invalid quantities remain rejected.
