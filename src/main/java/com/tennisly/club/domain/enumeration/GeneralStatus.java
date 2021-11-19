package com.tennisly.club.domain.enumeration;

/**
 * The GeneralStatus enumeration.
 */
public enum GeneralStatus {
    ACTIVE("Aktif"),
    PASSIVE("Pasif"),
    DELETED("Silinmiş");

    private final String value;

    GeneralStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
