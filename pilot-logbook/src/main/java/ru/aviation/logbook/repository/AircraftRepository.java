package ru.aviation.logbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aviation.logbook.domain.Aircraft;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    List<Aircraft> findByActiveTrueOrderByTypeNameAscRegistrationNumberAsc();

    List<Aircraft> findAllByOrderByTypeNameAscRegistrationNumberAsc();
}
