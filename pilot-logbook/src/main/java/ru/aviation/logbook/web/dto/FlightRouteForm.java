package ru.aviation.logbook.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FlightRouteForm {

    @NotBlank(message = "Укажите название маршрута")
    @Size(max = 150)
    private String label;

    private boolean active = true;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
