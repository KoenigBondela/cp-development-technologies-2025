package ru.aviation.logbook.domain;

public enum UserRole {
    CHIEF("Начальник авиакомпании"),
    PILOT("Пилот");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
