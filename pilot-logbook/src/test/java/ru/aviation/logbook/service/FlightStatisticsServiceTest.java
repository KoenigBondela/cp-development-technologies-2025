package ru.aviation.logbook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.aviation.logbook.domain.Aircraft;
import ru.aviation.logbook.domain.DayNight;
import ru.aviation.logbook.domain.FlightEntry;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.domain.UserRole;
import ru.aviation.logbook.repository.AircraftRepository;
import ru.aviation.logbook.repository.FlightEntryRepository;
import ru.aviation.logbook.repository.FlightRouteRepository;
import ru.aviation.logbook.repository.PilotUserRepository;
import ru.aviation.logbook.web.dto.AircraftTypeHoursRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(FlightStatisticsService.class)
@ActiveProfiles("test")
class FlightStatisticsServiceTest {

    @Autowired
    private FlightEntryRepository flightEntryRepository;

    @Autowired
    private PilotUserRepository pilotUserRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private FlightRouteRepository flightRouteRepository;

    @Autowired
    private FlightStatisticsService flightStatisticsService;

    private Long pilotId;

    @BeforeEach
    void setUp() {
        PilotUser pilot = new PilotUser();
        pilot.setUsername("stats");
        pilot.setPassword("x");
        pilot.setRole(UserRole.PILOT);
        pilot.setFullName("Статистика");
        pilot.setActive(true);
        pilotId = pilotUserRepository.save(pilot).getId();

        Aircraft ac = new Aircraft();
        ac.setTypeName("C172");
        ac.setRegistrationNumber("RA-1");
        ac.setActive(true);
        aircraftRepository.save(ac);

        FlightRoute route = new FlightRoute();
        route.setLabel("A — B");
        route.setActive(true);
        flightRouteRepository.save(route);
    }

    @Test
    void aggregatesTotalAndByAircraftType() {
        saveEntry("2025-01-10", "C172", new BigDecimal("1.00"));
        saveEntry("2025-02-15", "C172", new BigDecimal("2.00"));
        saveEntry("2025-02-20", "DA40", new BigDecimal("0.50"));

        BigDecimal total = flightStatisticsService.totalHoursBetween(
                pilotId, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
        assertEquals(new BigDecimal("3.50"), total);

        List<AircraftTypeHoursRow> byType = flightStatisticsService.hoursByAircraftTypeBetween(
                pilotId, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28));
        assertEquals(2, byType.size());
        assertEquals("C172", byType.get(0).aircraftType());
        assertEquals(new BigDecimal("2.00"), byType.get(0).flightHours());
    }

    private void saveEntry(String date, String type, BigDecimal hours) {
        PilotUser pilot = pilotUserRepository.findById(pilotId).orElseThrow();
        FlightEntry entry = new FlightEntry();
        entry.setPilot(pilot);
        entry.setFlightDate(LocalDate.parse(date));
        entry.setAircraftType(type);
        entry.setRegistrationNumber("RA-X");
        entry.setRoute("TEST");
        entry.setTakeoffTime(LocalTime.of(10, 0));
        entry.setLandingTime(LocalTime.of(11, 0));
        entry.setFlightHours(hours);
        entry.setDayNight(DayNight.DAY);
        flightEntryRepository.save(entry);
    }
}
