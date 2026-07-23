package dev.kartikm09.orders.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.kartikm09.orders.domain.PaymentStatus;
import dev.kartikm09.orders.domain.PaymentStatusEvent;
import dev.kartikm09.orders.infrastructure.InMemoryAuditRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class PaymentStatusServiceTest {
    private static final Clock CLOCK = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

    @Test
    void recordsStatusReturnedByMockedGateway() {
        var audit = new InMemoryAuditRepository();
        PaymentStatusGateway gateway =
                orderId -> new PaymentStatusEvent(orderId, PaymentStatus.AUTHORIZED, Instant.EPOCH);
        var service = new PaymentStatusService(gateway, audit, CLOCK);

        assertThat(service.refresh("order-1").status()).isEqualTo(PaymentStatus.AUTHORIZED);
        assertThat(audit.events()).singleElement().extracting("type").isEqualTo("PAYMENT_STATUS");
    }

    @Test
    void preservesRetryClassificationFromMockedFailure() {
        PaymentStatusGateway gateway =
                ignored -> {
                    throw new PaymentGatewayException("temporary timeout", FailureDisposition.RETRYABLE);
                };
        var service = new PaymentStatusService(gateway, new InMemoryAuditRepository(), CLOCK);

        assertThatThrownBy(() -> service.refresh("order-1"))
                .isInstanceOf(PaymentGatewayException.class)
                .extracting(error -> ((PaymentGatewayException) error).retryable())
                .isEqualTo(true);
    }

    @Test
    void classifiesMismatchedEventAsNonRetryable() {
        PaymentStatusGateway gateway =
                ignored -> new PaymentStatusEvent("different-order", PaymentStatus.PENDING, Instant.EPOCH);
        var service = new PaymentStatusService(gateway, new InMemoryAuditRepository(), CLOCK);

        assertThatThrownBy(() -> service.refresh("order-1"))
                .isInstanceOf(PaymentGatewayException.class)
                .extracting(error -> ((PaymentGatewayException) error).disposition())
                .isEqualTo(FailureDisposition.NON_RETRYABLE);
    }
}
