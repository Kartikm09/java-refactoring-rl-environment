package lab;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class AuditLog {
    private final List<String> events = new CopyOnWriteArrayList<>();
    public void append(String event) { events.add(event); }
    public List<String> events() { return List.copyOf(events); }
}
