package com.hoteldb.spring.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Совместимость с существующей servlet-лабой: пароли в БД в открытом виде.
 * Только для учебного проекта.
 */
public final class LabPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        return rawPassword.toString().equals(encodedPassword);
    }
}
