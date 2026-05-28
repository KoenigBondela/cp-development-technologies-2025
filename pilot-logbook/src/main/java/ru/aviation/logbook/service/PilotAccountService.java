package ru.aviation.logbook.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.domain.UserRole;
import ru.aviation.logbook.repository.FlightEntryRepository;
import ru.aviation.logbook.repository.PilotUserRepository;
import ru.aviation.logbook.web.dto.PilotProfileForm;
import ru.aviation.logbook.web.dto.PilotRegisterForm;
import ru.aviation.logbook.web.dto.PilotSummaryRow;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PilotAccountService {

    private final PilotUserRepository pilotUserRepository;
    private final FlightEntryRepository flightEntryRepository;
    private final PasswordEncoder passwordEncoder;

    public PilotAccountService(
            PilotUserRepository pilotUserRepository,
            FlightEntryRepository flightEntryRepository,
            PasswordEncoder passwordEncoder) {
        this.pilotUserRepository = pilotUserRepository;
        this.flightEntryRepository = flightEntryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<PilotSummaryRow> listPilotSummaries() {
        return pilotUserRepository.findAllPilotsForChief().stream()
                .map(p -> new PilotSummaryRow(
                        p.getId(),
                        p.getFullName(),
                        p.getUsername(),
                        p.getLicenseNumber(),
                        p.isActive(),
                        flightEntryRepository.sumFlightHoursByPilot(p.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public PilotUser requirePilot(Long id) {
        PilotUser pilot = pilotUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пилот не найден"));
        if (pilot.getRole() != UserRole.PILOT) {
            throw new IllegalArgumentException("Выбранный пользователь не является пилотом");
        }
        return pilot;
    }

    @Transactional
    public PilotUser registerPilot(PilotRegisterForm form) {
        String username = form.getUsername().trim();
        if (pilotUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Такой логин уже занят");
        }
        PilotUser pilot = new PilotUser();
        pilot.setUsername(username);
        pilot.setPassword(passwordEncoder.encode(form.getPassword()));
        pilot.setRole(UserRole.PILOT);
        pilot.setFullName(form.getFullName().trim());
        pilot.setLicenseNumber(trimToNull(form.getLicenseNumber()));
        pilot.setPhone(trimToNull(form.getPhone()));
        pilot.setEmail(trimToNull(form.getEmail()));
        pilot.setActive(true);
        return pilotUserRepository.save(pilot);
    }

    @Transactional
    public PilotUser createPilotByChief(PilotProfileForm form, String rawPassword) {
        if (pilotUserRepository.existsByUsername(form.getUsername().trim())) {
            throw new IllegalArgumentException("Такой логин уже занят");
        }
        PilotUser pilot = new PilotUser();
        applyProfile(pilot, form);
        pilot.setPassword(passwordEncoder.encode(rawPassword));
        pilot.setRole(UserRole.PILOT);
        pilot.setActive(true);
        return pilotUserRepository.save(pilot);
    }

    @Transactional
    public PilotUser updatePilotProfile(Long id, PilotProfileForm form) {
        PilotUser pilot = requirePilot(id);
        String newUsername = form.getUsername().trim();
        if (!pilot.getUsername().equals(newUsername)
                && pilotUserRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Такой логин уже занят");
        }
        applyProfile(pilot, form);
        return pilotUserRepository.save(pilot);
    }

    @Transactional
    public void updateOwnProfile(PilotUser current, PilotProfileForm form) {
        applyProfile(current, form);
        pilotUserRepository.save(current);
    }

    @Transactional
    public void changePassword(PilotUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        pilotUserRepository.save(user);
    }

    @Transactional
    public void setPilotActive(Long id, boolean active) {
        PilotUser pilot = requirePilot(id);
        pilot.setActive(active);
        pilotUserRepository.save(pilot);
    }

    @Transactional(readOnly = true)
    public BigDecimal totalHoursForPilot(Long pilotId) {
        return flightEntryRepository.sumFlightHoursByPilot(pilotId);
    }

    public static void copyToProfileForm(PilotUser pilot, PilotProfileForm form) {
        form.setUsername(pilot.getUsername());
        form.setFullName(pilot.getFullName());
        form.setLicenseNumber(pilot.getLicenseNumber());
        form.setPhone(pilot.getPhone());
        form.setEmail(pilot.getEmail());
        form.setActive(pilot.isActive());
    }

    private void applyProfile(PilotUser pilot, PilotProfileForm form) {
        pilot.setUsername(form.getUsername().trim());
        pilot.setFullName(form.getFullName().trim());
        pilot.setLicenseNumber(trimToNull(form.getLicenseNumber()));
        pilot.setPhone(trimToNull(form.getPhone()));
        pilot.setEmail(trimToNull(form.getEmail()));
        if (form.getActive() != null) {
            pilot.setActive(form.getActive());
        }
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
