package dev.kartikm09.orders.domain;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PricingEngineTest {
    @ParameterizedTest
    @CsvSource({"1,1000", "2,2000", "5,5000"})
    void preservesSubtotalWhenNoRulesApply(int quantity, long expected) {
        var engine = new PricingEngine(List.of());
        assertThat(engine.price(List.of(new OrderLine("SKU", quantity, 1000)))).isEqualTo(expected);
    }
}
