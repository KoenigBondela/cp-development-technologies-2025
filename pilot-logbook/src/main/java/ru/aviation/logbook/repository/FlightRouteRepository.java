package ru.aviation.logbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aviation.logbook.domain.FlightRoute;

import java.util.List;

public interface FlightRouteRepository extends JpaRepository<FlightRoute, Long> {

    List<FlightRoute> findByActiveTrueOrderByLabelAsc();

    List<FlightRoute> findAllByOrderByLabelAsc();
}
