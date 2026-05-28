package ru.aviation.logbook.web.dto;

import jakarta.validation.constraints.NotNull;
import ru.aviation.logbook.domain.DayNight;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightEntryForm {

    @NotNull(message = "Укажите дату полёта")
    private LocalDate flightDate;

    @NotNull(message = "Выберите воздушное судно")
    private Long aircraftId;

    @NotNull(message = "Выберите маршрут")
    private Long routeId;

    @NotNull(message = "Укажите время взлёта")
    private LocalTime takeoffTime;

    @NotNull(message = "Укажите время посадки")
    private LocalTime landingTime;

    @NotNull(message = "Укажите время суток")
    private DayNight dayNight;

    private String notes;

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public Long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public LocalTime getTakeoffTime() {
        return takeoffTime;
    }

    public void setTakeoffTime(LocalTime takeoffTime) {
        this.takeoffTime = takeoffTime;
    }

    public LocalTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(LocalTime landingTime) {
        this.landingTime = landingTime;
    }

    public DayNight getDayNight() {
        return dayNight;
    }

    public void setDayNight(DayNight dayNight) {
        this.dayNight = dayNight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
