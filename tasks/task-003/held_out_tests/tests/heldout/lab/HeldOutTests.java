package lab;
import pricing.PricingEngine;
public final class HeldOutTests {
    private HeldOutTests() { }
    public static void main(String[] args) {
        PricingEngine engine = new PricingEngine();
        String[] tiers = {"GOLD", "SILVER", "NONE"};
        for (String tier : tiers) for (int quantity : new int[]{1, 9, 10, 11}) for (boolean coupon : new boolean[]{false, true}) {
            long expected = reference(10000, quantity, tier, coupon);
            if (engine.price(10000, quantity, tier, coupon) != expected) throw new AssertionError(tier + quantity + coupon);
        }
        System.out.println("held-out golden master passed");
    }
    private static long reference(long subtotal, int quantity, String tier, boolean coupon) {
        long total = subtotal;
        if ("GOLD".equals(tier)) total = total * 90 / 100; else if ("SILVER".equals(tier)) total = total * 95 / 100;
        if (quantity >= 10) total -= 500; if (coupon) total -= 250; return Math.max(0, total);
    }
}
