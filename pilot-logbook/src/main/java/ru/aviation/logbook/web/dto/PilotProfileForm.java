package ru.aviation.logbook.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PilotProfileForm {

    @NotBlank(message = "Укажите логин")
    @Size(max = 80)
    private String username;

    @NotBlank(message = "Укажите ФИО")
    @Size(max = 150)
    private String fullName;

    @Size(max = 40)
    private String licenseNumber;

    @Size(max = 30)
    private String phone;

    @Size(max = 120)
    private String email;

    private Boolean active;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
