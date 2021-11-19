package com.tennisly.club.domain.enumeration;

/**
 * The Level enumeration.
 */
public enum Level {
    BEGINNER("Başlangıç"),
    INTERMEDIATE("Orta"),
    ADVANCED("İyi"),
    PROFICIENT("Profesyonel");

    private final String value;

    Level(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
