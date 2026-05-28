package ru.aviation.logbook.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Простое хранение пароля (для учебного проекта). В production — BCrypt.
 */
public class PlainPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}
