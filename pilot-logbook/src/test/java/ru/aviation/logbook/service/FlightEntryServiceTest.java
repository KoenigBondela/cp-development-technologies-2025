package ru.aviation.logbook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.aviation.logbook.domain.Aircraft;
import ru.aviation.logbook.domain.DayNight;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.domain.UserRole;
import ru.aviation.logbook.repository.AircraftRepository;
import ru.aviation.logbook.repository.FlightRouteRepository;
import ru.aviation.logbook.repository.PilotUserRepository;
import ru.aviation.logbook.web.dto.FlightEntryForm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({FlightEntryService.class, ReferenceDataService.class})
@ActiveProfiles("test")
class FlightEntryServiceTest {

    @Autowired
    private FlightEntryService flightEntryService;

    @Autowired
    private PilotUserRepository pilotUserRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private FlightRouteRepository flightRouteRepository;

    private PilotUser pilot;
    private Long aircraftId;
    private Long routeId;

    @BeforeEach
    void setUp() {
        pilot = new PilotUser();
        pilot.setUsername("testpilot");
        pilot.setPassword("pass");
        pilot.setRole(UserRole.PILOT);
        pilot.setFullName("Тестов Тест");
        pilot.setActive(true);
        pilot = pilotUserRepository.save(pilot);

        Aircraft aircraft = new Aircraft();
        aircraft.setTypeName("Cessna 172");
        aircraft.setRegistrationNumber("RA-00001");
        aircraft.setActive(true);
        aircraftId = aircraftRepository.save(aircraft).getId();

        FlightRoute route = new FlightRoute();
        route.setLabel("Москва — Казань");
        route.setActive(true);
        routeId = flightRouteRepository.save(route).getId();
    }

    @Test
    void createsEntryWithAutoCalculatedHours() {
        FlightEntryForm form = baseForm();
        form.setTakeoffTime(LocalTime.of(9, 0));
        form.setLandingTime(LocalTime.of(10, 30));

        var saved = flightEntryService.create(pilot, form);
        assertEquals(new BigDecimal("1.50"), saved.getFlightHours());
        assertEquals("Cessna 172", saved.getAircraftType());
    }

    @Test
    void requiresValidAircraft() {
        FlightEntryForm form = baseForm();
        form.setAircraftId(9999L);
        assertThrows(IllegalArgumentException.class, () -> flightEntryService.create(pilot, form));
    }

    private FlightEntryForm baseForm() {
        FlightEntryForm form = new FlightEntryForm();
        form.setFlightDate(LocalDate.of(2025, 5, 1));
        form.setAircraftId(aircraftId);
        form.setRouteId(routeId);
        form.setTakeoffTime(LocalTime.of(10, 0));
        form.setLandingTime(LocalTime.of(11, 0));
        form.setDayNight(DayNight.DAY);
        return form;
    }
}
