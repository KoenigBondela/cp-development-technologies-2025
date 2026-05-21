package com.hoteldb.spring.web.dto;

import com.hoteldb.spring.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserForm {

    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;

    @NotNull
    private UserRole role = UserRole.USER;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
