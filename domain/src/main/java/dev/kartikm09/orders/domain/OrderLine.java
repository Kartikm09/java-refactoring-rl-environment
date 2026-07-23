package dev.kartikm09.orders.domain;

import java.util.Objects;

public record OrderLine(String sku, int quantity, long unitPriceCents) {
    public OrderLine {
        Objects.requireNonNull(sku, "sku");
        if (sku.isBlank() || quantity <= 0 || unitPriceCents < 0) {
            throw new IllegalArgumentException("invalid order line");
        }
    }

    public long subtotalCents() {
        return Math.multiplyExact(quantity, unitPriceCents);
    }
}
