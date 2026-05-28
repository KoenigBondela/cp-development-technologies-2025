package ru.aviation.logbook.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "flight_entries")
public class FlightEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pilot_id", nullable = false)
    private PilotUser pilot;

    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    @Column(name = "aircraft_type", nullable = false, length = 80)
    private String aircraftType;

    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber;

    @Column(nullable = false, length = 150)
    private String route;

    @Column(name = "takeoff_time", nullable = false)
    private LocalTime takeoffTime;

    @Column(name = "landing_time", nullable = false)
    private LocalTime landingTime;

    @Column(name = "flight_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal flightHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_night", nullable = false, length = 10, columnDefinition = "varchar(10)")
    private DayNight dayNight;

    @Column(length = 2000)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PilotUser getPilot() {
        return pilot;
    }

    public void setPilot(PilotUser pilot) {
        this.pilot = pilot;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public BigDecimal getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(BigDecimal flightHours) {
        this.flightHours = flightHours;
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
