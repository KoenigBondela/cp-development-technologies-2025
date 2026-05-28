package ru.aviation.logbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aviation.logbook.domain.Aircraft;
import ru.aviation.logbook.repository.AircraftRepository;
import ru.aviation.logbook.web.dto.AircraftForm;

import java.util.List;

@Service
public class AircraftCatalogService {

    private final AircraftRepository aircraftRepository;

    public AircraftCatalogService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Transactional(readOnly = true)
    public List<Aircraft> findAll() {
        return aircraftRepository.findAllByOrderByTypeNameAscRegistrationNumberAsc();
    }

    @Transactional(readOnly = true)
    public Aircraft requireById(Long id) {
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Самолёт не найден"));
    }

    @Transactional
    public Aircraft create(AircraftForm form) {
        String reg = normalizeReg(form.getRegistrationNumber());
        if (aircraftRepository.findAll().stream()
                .anyMatch(a -> a.getRegistrationNumber().equalsIgnoreCase(reg))) {
            throw new IllegalArgumentException("Самолёт с таким бортовым номером уже есть");
        }
        Aircraft aircraft = new Aircraft();
        applyForm(aircraft, form);
        return aircraftRepository.save(aircraft);
    }

    @Transactional
    public Aircraft update(Long id, AircraftForm form) {
        Aircraft aircraft = requireById(id);
        String reg = normalizeReg(form.getRegistrationNumber());
        aircraftRepository.findAll().stream()
                .filter(a -> !a.getId().equals(id))
                .filter(a -> a.getRegistrationNumber().equalsIgnoreCase(reg))
                .findAny()
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Самолёт с таким бортовым номером уже есть");
                });
        applyForm(aircraft, form);
        return aircraftRepository.save(aircraft);
    }

    @Transactional
    public void deactivate(Long id) {
        Aircraft aircraft = requireById(id);
        aircraft.setActive(false);
        aircraftRepository.save(aircraft);
    }

    public static void copyToForm(Aircraft aircraft, AircraftForm form) {
        form.setTypeName(aircraft.getTypeName());
        form.setRegistrationNumber(aircraft.getRegistrationNumber());
        form.setActive(aircraft.isActive());
    }

    private void applyForm(Aircraft aircraft, AircraftForm form) {
        aircraft.setTypeName(form.getTypeName().trim());
        aircraft.setRegistrationNumber(normalizeReg(form.getRegistrationNumber()));
        aircraft.setActive(form.isActive());
    }

    private static String normalizeReg(String reg) {
        return reg.trim().toUpperCase();
    }
}
