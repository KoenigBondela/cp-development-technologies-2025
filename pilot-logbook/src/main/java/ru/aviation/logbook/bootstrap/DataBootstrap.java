package ru.aviation.logbook.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.aviation.logbook.domain.Aircraft;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.domain.UserRole;
import ru.aviation.logbook.repository.AircraftRepository;
import ru.aviation.logbook.repository.FlightRouteRepository;
import ru.aviation.logbook.repository.PilotUserRepository;

@Component
@Order(1)
public class DataBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataBootstrap.class);

    private final PilotUserRepository pilotUserRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightRouteRepository flightRouteRepository;

    public DataBootstrap(
            PilotUserRepository pilotUserRepository,
            AircraftRepository aircraftRepository,
            FlightRouteRepository flightRouteRepository) {
        this.pilotUserRepository = pilotUserRepository;
        this.aircraftRepository = aircraftRepository;
        this.flightRouteRepository = flightRouteRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        ensureChiefExists();
        ensureReferenceData();
    }

    private void ensureChiefExists() {
        if (pilotUserRepository.findByUsername("chief").isPresent()) {
            return;
        }
        PilotUser chief = new PilotUser();
        chief.setUsername("chief");
        chief.setPassword("chief");
        chief.setRole(UserRole.CHIEF);
        chief.setFullName("Иванов Иван Иванович");
        chief.setActive(true);
        pilotUserRepository.save(chief);
        log.warn("Создан начальник по умолчанию: chief / chief");
    }

    private void ensureReferenceData() {
        if (aircraftRepository.count() > 0) {
            return;
        }
        saveAircraft("Cessna 172", "RA-67890");
        saveAircraft("Cessna 172", "RA-67891");
        saveAircraft("Diamond DA40", "RA-11223");
        saveAircraft("Як-52", "RA-02852");

        saveRoute("Москва (Внуково) — Санкт-Петербург (Пулково)");
        saveRoute("Москва (Внуково) — Казань");
        saveRoute("Москва (Внуково) — локальный полёт");
        saveRoute("Санкт-Петербург (Пулково) — Москва (Внуково)");
        saveRoute("Казань — Москва (Внуково)");
    }

    private void saveAircraft(String type, String reg) {
        Aircraft a = new Aircraft();
        a.setTypeName(type);
        a.setRegistrationNumber(reg);
        a.setActive(true);
        aircraftRepository.save(a);
    }

    private void saveRoute(String label) {
        FlightRoute r = new FlightRoute();
        r.setLabel(label);
        r.setActive(true);
        flightRouteRepository.save(r);
    }
}
