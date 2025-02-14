package com.company.artistmgmt.model.general;

import lombok.Getter;

@Getter
public enum Genre {
    GENRE_UNSPECIFIED(0),
    MB(1),
    COUNTRY(2),
    CLASSIC(3),
    ROCK(4),
    JAZZ(5);

    private final int value;

    Genre(int value) {
        this.value = value;
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
