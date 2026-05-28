package ru.aviation.logbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aviation.logbook.repository.FlightEntryRepository;
import ru.aviation.logbook.web.dto.AircraftTypeHoursRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FlightStatisticsService {

    private final FlightEntryRepository flightEntryRepository;

    public FlightStatisticsService(FlightEntryRepository flightEntryRepository) {
        this.flightEntryRepository = flightEntryRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal totalHoursBetween(Long pilotId, LocalDate from, LocalDate to) {
        validatePeriod(from, to);
        return flightEntryRepository.sumFlightHoursByPilotBetween(pilotId, from, to);
    }

    @Transactional(readOnly = true)
    public List<AircraftTypeHoursRow> hoursByAircraftTypeBetween(Long pilotId, LocalDate from, LocalDate to) {
        validatePeriod(from, to);
        return flightEntryRepository.sumFlightHoursByAircraftTypeForPilotBetween(pilotId, from, to).stream()
                .map(row -> new AircraftTypeHoursRow((String) row[0], (BigDecimal) row[1]))
                .toList();
    }

    private static void validatePeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Укажите начало и конец периода");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");
        }
    }
}
