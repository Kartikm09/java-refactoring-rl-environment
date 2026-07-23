package dev.kartikm09.orders.domain;

import java.util.List;

public final class PricingEngine {
    private final List<PricingRule> rules;

    public PricingEngine(List<PricingRule> rules) { this.rules = List.copyOf(rules); }

    public long price(List<OrderLine> lines) {
        long total = lines.stream().mapToLong(OrderLine::subtotalCents).sum();
        for (PricingRule rule : rules) { total = rule.apply(total, lines); }
        return total;
    }
}
