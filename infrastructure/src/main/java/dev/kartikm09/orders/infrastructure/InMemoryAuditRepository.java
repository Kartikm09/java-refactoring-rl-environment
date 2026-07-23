package dev.kartikm09.orders.infrastructure;

import dev.kartikm09.orders.application.AuditRepository;
import dev.kartikm09.orders.domain.AuditEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class InMemoryAuditRepository implements AuditRepository {
    private final List<AuditEvent> events = new CopyOnWriteArrayList<>();
    @Override public void append(AuditEvent event) { events.add(event); }
    public List<AuditEvent> events() { return List.copyOf(events); }
}
