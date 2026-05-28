package ru.aviation.logbook.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.aviation.logbook.repository.PilotUserRepository;

import java.util.List;

@Service
public class PilotUserDetailsService implements UserDetailsService {

    private final PilotUserRepository pilotUserRepository;

    public PilotUserDetailsService(PilotUserRepository pilotUserRepository) {
        this.pilotUserRepository = pilotUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var pilot = pilotUserRepository.findByUsernameAndActiveTrue(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        return new User(
                pilot.getUsername(),
                pilot.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + pilot.getRole().name())));
    }
}
