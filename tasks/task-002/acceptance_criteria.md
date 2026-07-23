# Acceptance Criteria

- Cancel a positive quantity no greater than the remaining line quantity.
- Restore exactly the cancelled quantity to inventory.
- Emit one structured audit event per successful request.
- Keep full-order cancellation behavior backward compatible.
