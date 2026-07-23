package dev.kartikm09.orders.application;

import dev.kartikm09.orders.domain.AuditEvent;

public interface AuditRepository { void append(AuditEvent event); }
