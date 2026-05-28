package ru.aviation.logbook.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

public final class FlightHoursCalculator {

    private FlightHoursCalculator() {
    }

    public static BigDecimal fromTimesRequired(LocalTime takeoff, LocalTime landing) {
        BigDecimal hours = fromTimes(takeoff, landing);
        if (hours == null) {
            throw new IllegalArgumentException("Укажите время взлёта и посадки");
        }
        return hours;
    }

    public static BigDecimal fromTimes(LocalTime takeoff, LocalTime landing) {
        if (takeoff == null || landing == null) {
            return null;
        }
        Duration duration;
        if (!landing.isBefore(takeoff)) {
            duration = Duration.between(takeoff, landing);
        } else {
            duration = Duration.between(takeoff, landing).plusHours(24);
        }
        long minutes = duration.toMinutes();
        return BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
}
