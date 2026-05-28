package ru.aviation.logbook.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AircraftForm {

    @NotBlank(message = "Укажите тип самолёта")
    @Size(max = 80)
    private String typeName;

    @NotBlank(message = "Укажите бортовой номер")
    @Size(max = 20)
    private String registrationNumber;

    private boolean active = true;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
