package com.hoteldb.spring.service;

import com.hoteldb.spring.domain.UserEntity;
import com.hoteldb.spring.domain.UserRole;
import com.hoteldb.spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccountService {

    private final UserRepository userRepository;

    public UserAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity registerUser(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password required");
        }
        String u = username.trim();
        if (userRepository.existsByUsername(u)) {
            throw new IllegalStateException("username already exists");
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(u);
        entity.setPassword(password);
        entity.setRole(UserRole.USER);
        return userRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public UserEntity requireByUsername(String username) {
        return userRepository.findByUsername(username.trim())
                .orElseThrow(() -> new IllegalStateException("user not found"));
    }
}
