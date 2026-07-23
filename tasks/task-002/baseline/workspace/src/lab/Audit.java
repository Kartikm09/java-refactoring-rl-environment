package lab;
import java.util.ArrayList;
import java.util.List;
public final class Audit {
    private final List<String> events = new ArrayList<>();
    public void append(String event) { events.add(event); }
    public List<String> events() { return List.copyOf(events); }
}
