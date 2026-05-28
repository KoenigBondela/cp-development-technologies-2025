package ru.aviation.logbook.web.dto;

import java.math.BigDecimal;

public record PilotSummaryRow(
        Long id,
        String fullName,
        String username,
        String licenseNumber,
        boolean active,
        BigDecimal totalFlightHours) {
}
