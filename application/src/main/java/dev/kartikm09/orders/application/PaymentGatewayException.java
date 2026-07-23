package dev.kartikm09.orders.application;

import java.io.Serial;
import java.util.Objects;

public final class PaymentGatewayException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final FailureDisposition disposition;

    public PaymentGatewayException(String message, FailureDisposition disposition) {
        super(message);
        this.disposition = Objects.requireNonNull(disposition, "disposition");
    }

    public FailureDisposition disposition() {
        return disposition;
    }

    public boolean retryable() {
        return disposition == FailureDisposition.RETRYABLE;
    }
}
