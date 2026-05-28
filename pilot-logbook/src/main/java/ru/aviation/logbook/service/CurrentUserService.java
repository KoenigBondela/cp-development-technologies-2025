package ru.aviation.logbook.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.repository.PilotUserRepository;

@Service
public class CurrentUserService {

    private final PilotUserRepository pilotUserRepository;

    public CurrentUserService(PilotUserRepository pilotUserRepository) {
        this.pilotUserRepository = pilotUserRepository;
    }

    public PilotUser requireCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Требуется вход в систему");
        }
        return pilotUserRepository.findByUsernameAndActiveTrue(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
    }
}
