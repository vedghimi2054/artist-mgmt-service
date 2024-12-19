package com.company.artistmgmt.model.general;

import lombok.Getter;

@Getter
public enum Gender {
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int value;

    Gender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean isValid(String genderName) {
        if (genderName == null) {
            return false;
        }
        try {
            Gender.valueOf(genderName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Gender fromValue(int value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }

    public static Gender parseGender(int genderInt) {

        return switch (genderInt) {
            case 1 -> MALE;
            case 2 -> FEMALE;
            case 3 -> OTHER;
            default -> throw new IllegalArgumentException("Invalid gender value: " + genderInt);
        };
    }
}
