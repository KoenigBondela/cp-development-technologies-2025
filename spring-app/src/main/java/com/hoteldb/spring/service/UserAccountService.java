package com.hoteldb.spring.service;

import com.hoteldb.spring.domain.UserEntity;
import com.hoteldb.spring.domain.UserRole;
import com.hoteldb.spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserRepository userRepository;

    public UserAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity register(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password is required");
        }
        String u = username.trim();
        if (userRepository.existsByUsernameAndDeletedFalse(u)) {
            throw new IllegalStateException("username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(u);
        user.setPassword(password);
        user.setRole(UserRole.USER);
        user.setDeleted(false);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        String u = username.trim();
        if (u.isEmpty() || password.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByUsernameAndDeletedFalse(u)
                .filter(user -> password.equals(user.getPassword()));
    }

    @Transactional(readOnly = true)
    public UserEntity requireByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username.trim())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }
}
