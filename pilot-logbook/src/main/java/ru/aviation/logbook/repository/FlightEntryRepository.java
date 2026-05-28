package ru.aviation.logbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.aviation.logbook.domain.FlightEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightEntryRepository extends JpaRepository<FlightEntry, Long> {

    List<FlightEntry> findByPilotIdOrderByFlightDateDescIdDesc(Long pilotId);

    Optional<FlightEntry> findByIdAndPilotId(Long id, Long pilotId);

    @Query("""
            SELECT COALESCE(SUM(f.flightHours), 0)
            FROM FlightEntry f
            WHERE f.pilot.id = :pilotId
            """)
    BigDecimal sumFlightHoursByPilot(@Param("pilotId") Long pilotId);

    @Query("""
            SELECT COALESCE(SUM(f.flightHours), 0)
            FROM FlightEntry f
            WHERE f.pilot.id = :pilotId
              AND f.flightDate BETWEEN :from AND :to
            """)
    BigDecimal sumFlightHoursByPilotBetween(
            @Param("pilotId") Long pilotId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    @Query("""
            SELECT f.aircraftType, SUM(f.flightHours)
            FROM FlightEntry f
            WHERE f.pilot.id = :pilotId
              AND f.flightDate BETWEEN :from AND :to
            GROUP BY f.aircraftType
            ORDER BY SUM(f.flightHours) DESC
            """)
    List<Object[]> sumFlightHoursByAircraftTypeForPilotBetween(
            @Param("pilotId") Long pilotId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
