package dev.kartikm09.orders.domain;

import java.util.List;

@FunctionalInterface
public interface PricingRule {
    long apply(long runningTotalCents, List<OrderLine> lines);
}
