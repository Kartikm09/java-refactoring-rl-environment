package dev.kartikm09.orders.app;

import com.sun.net.httpserver.HttpServer;
import dev.kartikm09.orders.api.OrderApi;
import dev.kartikm09.orders.application.OrderService;
import dev.kartikm09.orders.domain.OrderLine;
import dev.kartikm09.orders.infrastructure.InMemoryAuditRepository;
import dev.kartikm09.orders.infrastructure.InMemoryInventoryRepository;
import dev.kartikm09.orders.infrastructure.InMemoryOrderRepository;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;

public final class Main {
    private Main() { }
    public static void main(String[] args) throws IOException {
        OrderApi api = api();
        if (args.length == 1 && "--once".equals(args[0])) {
            System.out.println(api.create(new OrderApi.CreateOrderRequest("demo-key", "order-001", List.of(new OrderLine("SKU-DEMO", 2, 1500)))));
            return;
        }
        int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "8081"));
        String host = System.getenv().getOrDefault("APP_HOST", "127.0.0.1");
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/health", exchange -> {
            byte[] body = "{\"status\":\"ok\"}\n".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        System.out.println("order service listening on " + port);
    }
    private static OrderApi api() {
        var inventory = new InMemoryInventoryRepository(Map.of("SKU-DEMO", 10));
        var service = new OrderService(inventory, new InMemoryOrderRepository(), new InMemoryAuditRepository(), Clock.systemUTC());
        return new OrderApi(service);
    }
}
