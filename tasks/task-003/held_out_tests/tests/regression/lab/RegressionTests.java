package lab;
import pricing.PricingEngine;
public final class RegressionTests {
    private RegressionTests() { }
    public static void main(String[] args) {
        PricingEngine engine = new PricingEngine();
        if (engine.price(100, 10, "GOLD", true) != 0 || engine.price(0, 1, "NONE", false) != 0) throw new AssertionError();
        System.out.println("pricing floor regression passed");
    }
}
