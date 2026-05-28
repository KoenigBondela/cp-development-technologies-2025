package ru.aviation.logbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aviation.logbook.domain.Aircraft;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.repository.AircraftRepository;
import ru.aviation.logbook.repository.FlightRouteRepository;

import java.util.List;

@Service
public class ReferenceDataService {

    private final AircraftRepository aircraftRepository;
    private final FlightRouteRepository flightRouteRepository;

    public ReferenceDataService(
            AircraftRepository aircraftRepository,
            FlightRouteRepository flightRouteRepository) {
        this.aircraftRepository = aircraftRepository;
        this.flightRouteRepository = flightRouteRepository;
    }

    @Transactional(readOnly = true)
    public List<Aircraft> activeAircraft() {
        return aircraftRepository.findByActiveTrueOrderByTypeNameAscRegistrationNumberAsc();
    }

    @Transactional(readOnly = true)
    public List<FlightRoute> activeRoutes() {
        return flightRouteRepository.findByActiveTrueOrderByLabelAsc();
    }

    @Transactional(readOnly = true)
    public Aircraft requireAircraft(Long id) {
        return aircraftRepository.findById(id)
                .filter(Aircraft::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Воздушное судно не найдено"));
    }

    @Transactional(readOnly = true)
    public FlightRoute requireRoute(Long id) {
        return flightRouteRepository.findById(id)
                .filter(FlightRoute::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Маршрут не найден"));
    }
}
