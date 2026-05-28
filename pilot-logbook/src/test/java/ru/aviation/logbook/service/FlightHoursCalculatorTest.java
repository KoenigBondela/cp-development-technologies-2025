package ru.aviation.logbook.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FlightHoursCalculatorTest {

    @Test
    void calculatesSameDayDuration() {
        BigDecimal hours = FlightHoursCalculator.fromTimes(LocalTime.of(10, 0), LocalTime.of(11, 30));
        assertEquals(new BigDecimal("1.50"), hours);
    }

    @Test
    void calculatesOvernightDuration() {
        BigDecimal hours = FlightHoursCalculator.fromTimes(LocalTime.of(23, 0), LocalTime.of(1, 0));
        assertEquals(new BigDecimal("2.00"), hours);
    }

    @Test
    void returnsNullWhenTimesMissing() {
        assertNull(FlightHoursCalculator.fromTimes(null, LocalTime.of(12, 0)));
        assertNull(FlightHoursCalculator.fromTimes(LocalTime.of(12, 0), null));
    }
}
