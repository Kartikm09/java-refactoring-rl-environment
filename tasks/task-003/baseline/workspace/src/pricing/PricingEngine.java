package pricing;

public final class PricingEngine {
    public long price(long subtotal, int quantity, String customerTier, boolean coupon) {
        long total = subtotal;
        if ("GOLD".equals(customerTier)) { total = total * 90 / 100; }
        else if ("SILVER".equals(customerTier)) { total = total * 95 / 100; }
        if (quantity >= 10) { total -= 500; }
        if (coupon) { total -= 250; }
        return Math.max(0, total);
    }
}
