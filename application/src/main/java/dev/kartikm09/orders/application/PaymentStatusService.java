package dev.kartikm09.orders.application;

import dev.kartikm09.orders.domain.AuditEvent;
import dev.kartikm09.orders.domain.PaymentStatusEvent;
import java.time.Clock;
import java.util.Objects;

public final class PaymentStatusService {
    private final PaymentStatusGateway gateway;
    private final AuditRepository audit;
    private final Clock clock;

    public PaymentStatusService(PaymentStatusGateway gateway, AuditRepository audit, Clock clock) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
        this.audit = Objects.requireNonNull(audit, "audit");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public PaymentStatusEvent refresh(String orderId) {
        PaymentStatusEvent event = gateway.fetch(orderId);
        if (!orderId.equals(event.orderId())) {
            throw new PaymentGatewayException(
                    "gateway returned a different order ID", FailureDisposition.NON_RETRYABLE);
        }
        audit.append(new AuditEvent(orderId, "PAYMENT_STATUS", event.status().name(), clock.instant()));
        return event;
    }
}
