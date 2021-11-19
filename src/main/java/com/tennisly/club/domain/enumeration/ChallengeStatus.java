package com.tennisly.club.domain.enumeration;

/**
 * The ChallengeStatus enumeration.
 */
public enum ChallengeStatus {
    REQUESTED("Teklif"),
    ACCEPTED("Kabul"),
    REJECTED("Red");

    private final String value;

    ChallengeStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
