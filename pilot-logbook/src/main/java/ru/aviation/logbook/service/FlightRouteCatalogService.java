package ru.aviation.logbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.repository.FlightRouteRepository;
import ru.aviation.logbook.web.dto.FlightRouteForm;

import java.util.List;

@Service
public class FlightRouteCatalogService {

    private final FlightRouteRepository flightRouteRepository;

    public FlightRouteCatalogService(FlightRouteRepository flightRouteRepository) {
        this.flightRouteRepository = flightRouteRepository;
    }

    @Transactional(readOnly = true)
    public List<FlightRoute> findAll() {
        return flightRouteRepository.findAllByOrderByLabelAsc();
    }

    @Transactional(readOnly = true)
    public FlightRoute requireById(Long id) {
        return flightRouteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Маршрут не найден"));
    }

    @Transactional
    public FlightRoute create(FlightRouteForm form) {
        FlightRoute route = new FlightRoute();
        applyForm(route, form);
        return flightRouteRepository.save(route);
    }

    @Transactional
    public FlightRoute update(Long id, FlightRouteForm form) {
        FlightRoute route = requireById(id);
        applyForm(route, form);
        return flightRouteRepository.save(route);
    }

    @Transactional
    public void deactivate(Long id) {
        FlightRoute route = requireById(id);
        route.setActive(false);
        flightRouteRepository.save(route);
    }

    public static void copyToForm(FlightRoute route, FlightRouteForm form) {
        form.setLabel(route.getLabel());
        form.setActive(route.isActive());
    }

    private void applyForm(FlightRoute route, FlightRouteForm form) {
        route.setLabel(form.getLabel().trim());
        route.setActive(form.isActive());
    }
}
