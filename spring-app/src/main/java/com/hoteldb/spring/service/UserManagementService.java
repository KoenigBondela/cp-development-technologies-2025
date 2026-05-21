package com.hoteldb.spring.service;

import com.hoteldb.spring.domain.UserEntity;
import com.hoteldb.spring.domain.UserRole;
import com.hoteldb.spring.repository.UserRepository;
import com.hoteldb.spring.web.dto.UserForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserManagementService {

    private final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAllActive() {
        return userRepository.findAllByDeletedFalseOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public UserEntity requireActiveById(Integer id) {
        return userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserEntity requireActiveByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username.trim())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Transactional
    public UserEntity create(UserForm form) {
        if (userRepository.existsByUsernameAndDeletedFalse(form.getUsername().trim())) {
            throw new IllegalStateException("username already exists");
        }
        UserEntity user = new UserEntity();
        user.setUsername(form.getUsername().trim());
        user.setPassword(form.getPassword());
        user.setRole(form.getRole() == null ? UserRole.USER : form.getRole());
        user.setDeleted(false);
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity update(Integer id, UserForm form) {
        UserEntity user = requireActiveById(id);
        String newUsername = form.getUsername().trim();
        if (!user.getUsername().equals(newUsername)
                && userRepository.existsByUsernameAndDeletedFalse(newUsername)) {
            throw new IllegalStateException("username already exists");
        }
        user.setUsername(newUsername);
        user.setPassword(form.getPassword());
        user.setRole(form.getRole() == null ? UserRole.USER : form.getRole());
        return userRepository.save(user);
    }

    @Transactional
    public void softDelete(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        if (user.isDeleted()) {
            return;
        }
        if (user.getRole() == UserRole.ADMIN && "admin".equals(user.getUsername())) {
            throw new IllegalStateException("Нельзя удалить основного администратора");
        }
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Transactional
    public UserEntity updateOwnProfile(String username, String newPassword) {
        UserEntity user = requireActiveByUsername(username);
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("password required");
        }
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
