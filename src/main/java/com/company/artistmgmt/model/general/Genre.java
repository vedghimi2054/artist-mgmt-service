package com.company.artistmgmt.model.general;

public enum Genre {
    MB(1),
    COUNTRY(2),
    CLASSIC(3),
    ROCK(4),
    JAZZ(5);

    private final int value;

    Genre(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Genre fromValue(int value) {
        for (Genre genre : Genre.values()) {
            if (genre.getValue() == value) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Invalid genre value: " + value);
    }
}
