package lab;
import java.nio.file.Files;
import java.nio.file.Path;
import pricing.PricingEngine;
public final class PublicTests {
    private PublicTests() { }
    public static void main(String[] args) throws Exception {
        String source = Files.readString(Path.of("src/pricing/PricingEngine.java"));
        if (source.contains("if (\"GOLD\"") || source.contains("else if")) throw new AssertionError("conditional pricing chain remains");
        PricingEngine engine = new PricingEngine();
        if (engine.price(10000, 1, "GOLD", false) != 9000 || engine.price(10000, 10, "NONE", true) != 9250) throw new AssertionError();
        System.out.println("public pricing refactor tests passed");
    }
}
