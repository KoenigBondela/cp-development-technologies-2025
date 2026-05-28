package ru.aviation.logbook.web.dto;

import java.math.BigDecimal;

public record AircraftTypeHoursRow(String aircraftType, BigDecimal flightHours) {
}
