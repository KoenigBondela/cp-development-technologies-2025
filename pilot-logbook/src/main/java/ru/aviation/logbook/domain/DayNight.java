package ru.aviation.logbook.domain;

public enum DayNight {
    DAY("Днём"),
    NIGHT("Ночью");

    private final String label;

    DayNight(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
