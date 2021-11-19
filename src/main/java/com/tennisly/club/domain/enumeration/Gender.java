package com.tennisly.club.domain.enumeration;

/**
 * The Gender enumeration.
 */
public enum Gender {
    MAN("Erkek"),
    WOMEN("Kadın");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
