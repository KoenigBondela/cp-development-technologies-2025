package ru.aviation.logbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aviation.logbook.domain.Aircraft;
import ru.aviation.logbook.domain.FlightEntry;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.repository.FlightEntryRepository;
import ru.aviation.logbook.web.dto.FlightEntryForm;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FlightEntryService {

    private final FlightEntryRepository flightEntryRepository;
    private final ReferenceDataService referenceDataService;

    public FlightEntryService(
            FlightEntryRepository flightEntryRepository,
            ReferenceDataService referenceDataService) {
        this.flightEntryRepository = flightEntryRepository;
        this.referenceDataService = referenceDataService;
    }

    @Transactional(readOnly = true)
    public List<FlightEntry> findForPilot(Long pilotId) {
        return flightEntryRepository.findByPilotIdOrderByFlightDateDescIdDesc(pilotId);
    }

    @Transactional(readOnly = true)
    public BigDecimal totalFlightHours(Long pilotId) {
        return flightEntryRepository.sumFlightHoursByPilot(pilotId);
    }

    @Transactional(readOnly = true)
    public FlightEntry requireForPilot(Long id, Long pilotId) {
        return flightEntryRepository.findByIdAndPilotId(id, pilotId)
                .orElseThrow(() -> new IllegalArgumentException("Запись о полёте не найдена"));
    }

    @Transactional
    public FlightEntry create(PilotUser pilot, FlightEntryForm form) {
        FlightEntry entry = new FlightEntry();
        entry.setPilot(pilot);
        applyForm(entry, form);
        return flightEntryRepository.save(entry);
    }

    @Transactional
    public FlightEntry update(Long id, Long pilotId, FlightEntryForm form) {
        FlightEntry entry = requireForPilot(id, pilotId);
        applyForm(entry, form);
        return flightEntryRepository.save(entry);
    }

    @Transactional
    public void delete(Long id, Long pilotId) {
        FlightEntry entry = requireForPilot(id, pilotId);
        flightEntryRepository.delete(entry);
    }

    public void applyForm(FlightEntry entry, FlightEntryForm form) {
        Aircraft aircraft = referenceDataService.requireAircraft(form.getAircraftId());
        FlightRoute route = referenceDataService.requireRoute(form.getRouteId());

        entry.setFlightDate(form.getFlightDate());
        entry.setAircraft(aircraft);
        entry.setAircraftType(aircraft.getTypeName());
        entry.setRegistrationNumber(aircraft.getRegistrationNumber());
        entry.setRoute(route.getLabel());
        entry.setTakeoffTime(form.getTakeoffTime());
        entry.setLandingTime(form.getLandingTime());
        entry.setFlightHours(FlightHoursCalculator.fromTimesRequired(
                form.getTakeoffTime(), form.getLandingTime()));
        entry.setDayNight(form.getDayNight());
        entry.setNotes(trimToNull(form.getNotes()));
    }

    public static void copyToForm(FlightEntry entry, FlightEntryForm form) {
        form.setFlightDate(entry.getFlightDate());
        if (entry.getAircraft() != null) {
            form.setAircraftId(entry.getAircraft().getId());
        }
        form.setTakeoffTime(entry.getTakeoffTime());
        form.setLandingTime(entry.getLandingTime());
        form.setDayNight(entry.getDayNight());
        form.setNotes(entry.getNotes());
        // routeId resolved in controller from route label if needed
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
