package com.company.artistmgmt.model.general;

public enum Genre {
    MB("mb"),
    COUNTRY("country"),
    CLASSIC("classic"),
    ROCK("rock"),
    JAZZ("jazz");

    private final String value;

    Genre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Genre fromValue(String value) {
        for (Genre genre : Genre.values()) {
            if (genre.getValue().equalsIgnoreCase(value)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Invalid genre value: " + value);
    }
}
